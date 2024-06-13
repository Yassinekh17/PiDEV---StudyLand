<?php

namespace App\Controller;
use Symfony\Component\Security\Core\Exception\AuthenticationException;
use App\Entity\User;
use Symfony\Component\HttpFoundation\RedirectResponse;
use App\Form\AddUserType;
use App\Form\EditUserType;
use App\Form\UserType;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;

use App\Repository\UserRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\Persistence\ManagerRegistry;
use Laminas\Code\Generator\EnumGenerator\Name;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Security\Core\Security;

use App\Form\LoginFormType;
use App\Service\EmailService;
use Symfony\Component\Security\Core\Authorization\Voter\AuthenticatedVoter;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;

class UserController extends AbstractController
{
    
//home
    #[Route('/', name: "home")]
    public function home(): Response
    {
        return $this->render('base.html.twig');
    }
  
//COTé CLIENT 


#[Route('/EditClient/{id}', name: 'editProfile')]
public function editClient($id, UserPasswordEncoderInterface $passwordEncoder, ManagerRegistry $manager, Request $request, UserRepository $repo): Response
{  $em = $manager->getManager();
    $user = $repo->find($id);

    if (!$user) {
        throw $this->createNotFoundException('Utilisateur non trouvé');
    }
    $form = $this->createForm(UserType::class, $user);
    $form->get('password')->setData($user->getPassword());
    $form->get('confirmerPassword')->setData($user->getPassword());
    $form->add('password', PasswordType::class, [
        'disabled' => true, 
        'required' => false, 
        'attr' => [ 'style' => 'display:none' ] 
    ]);
    $form->add('confirmerPassword', PasswordType::class, [
        'disabled' => true,
        'required' => false,
        'attr' => [ 'style' => 'display:none' ] 
    
    ]);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
        // Mise à jour des données de l'utilisateur
        $user->setNom($form->get('nom')->getData());
        $user->setPrenom($form->get('prenom')->getData());
        $user->setEmail($form->get('email')->getData());
        
        // Traitement de l'image si elle est modifiée
        $image = $form->get('image')->getData();
        if ($image) {
            $originalFilename = pathinfo($image->getClientOriginalName(), PATHINFO_FILENAME);
            $newFilename = $originalFilename.'.'.$image->guessExtension();
            try {
                $image->move($this->getParameter('image_directory'), $newFilename);
                $user->setImage($newFilename);
            } catch (FileException $e) {
                // Gérer les exceptions de téléchargement de fichier
            }
        }
        $em->flush();

        return $this->render('client/profile.html.twig', ['user'=>$user]);
    }
    return $this->render('Client/EditProfile.html.twig', [
        'form' => $form->createView(),
        'user' => $user,
    ]);
}
#[Route('/EditAdmin/{id}', name: 'editAdmin')]
public function editAdmin($id, UserPasswordEncoderInterface $passwordEncoder, SessionInterface $session ,ManagerRegistry $manager, Request $request, UserRepository $repo): Response
{  $em = $manager->getManager();
    $user = $repo->find($id);

    if (!$user) {
        throw $this->createNotFoundException('Utilisateur non trouvé');
    }
    $form = $this->createForm(UserType::class, $user);
    $form->get('password')->setData($user->getPassword());
    $form->get('confirmerPassword')->setData($user->getPassword());
    $form->add('password', PasswordType::class, [
        'disabled' => true, 
        'required' => false, 
        'attr' => [ 'style' => 'display:none' ] 
    ]);
    $form->add('confirmerPassword', PasswordType::class, [
        'disabled' => true,
        'required' => false,
        'attr' => [ 'style' => 'display:none' ] 
    
    ]);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
        // Mise à jour des données de l'utilisateur
        $user->setNom($form->get('nom')->getData());
        $user->setPrenom($form->get('prenom')->getData());
        $user->setEmail($form->get('email')->getData());
        
        // Traitement de l'image si elle est modifiée
        $image = $form->get('image')->getData();
        if ($image) {
            $originalFilename = pathinfo($image->getClientOriginalName(), PATHINFO_FILENAME);
            $newFilename = $originalFilename.'.'.$image->guessExtension();
            try {
                $image->move($this->getParameter('image_directory'), $newFilename);
                $user->setImage($newFilename);
            } catch (FileException $e) {
                // Gérer les exceptions de téléchargement de fichier
            }
        }
        $em->flush();
        $session->set('user', $user);


        return $this->redirectToRoute('dash',['user'=>$user]);
    }
    return $this->render('admin/EditAdmin.html.twig', [
        'form' => $form->createView(),
        'user' => $user,
    ]);
}
 
#[Route('/login', name: 'login', methods: ['GET', 'POST'])]
public function login(Request $request, AuthenticationUtils $authenticationUtils, UserPasswordEncoderInterface $passwordEncoder, Security $security, SessionInterface $session): Response
{
    // Vérification du reCAPTCHA
    $error = $authenticationUtils->getLastAuthenticationError();
    $lastUsername = $authenticationUtils->getLastUsername();
    
    if ($request->isMethod('POST')) {
        $recaptchaResponse = $request->request->get('g-recaptcha-response');
        
        // Valider la réponse reCAPTCHA
        $isRecaptchaValid = $this->validateRecaptcha($recaptchaResponse);
        
        if (!$isRecaptchaValid) {
            // Si la validation du reCAPTCHA échoue, renvoyer une erreur
            $error = 'Veuillez prouver que vous n\'êtes pas un robot.';
        } else {
            // Le reCAPTCHA est valide, continuer avec la vérification des informations d'identification de l'utilisateur
            $email = $request->request->get('email');
            $password = $request->request->get('password');
            $user = $this->getDoctrine()->getRepository(User::class)->findOneBy(['email' => $email]);
        
            if (!$user) {
                $error = 'Email ou mot de passe incorrect';
            } elseif (!$passwordEncoder->isPasswordValid($user, $password)) {
                $error = 'Email ou mot de passe incorrect';
            } else {
                // Vérifier si le reCAPTCHA est également validé
                if ($isRecaptchaValid) {
                    $session->set('user', $user);

                    // Redirection vers le profil approprié en fonction du rôle de l'utilisateur
                    if ($user->getRole() === 'Apprenant') {
                        return $this->render('client/profile.html.twig', ['user'=>$user]);
                    } elseif ($user->getRole() === 'Admin') {
                        return $this->redirectToRoute('dash');
                    } elseif ($user->getRole() === 'Formateur') {
                        return $this->render('client/profileFormateur.html.twig', ['user'=>$user]);
                    }
                } else {
                    // Si le reCAPTCHA n'est pas valide, afficher une erreur
                    $error = 'Veuillez prouver que vous n\'êtes pas un robot.';
                }
            }
        }
    }

    // Si la connexion échoue ou si le reCAPTCHA n'a pas été validé, afficher à nouveau la page de connexion avec les erreurs appropriées
    $user = $security->getUser();
    return $this->render('login.html.twig', [
        'error' => $error,
        'last_username' => $lastUsername,
        'user' => $user,
    ]);
}
//logout 
    #[Route('/logout', name: 'logout')]
    public function logout(Request $request, SessionInterface $session)
    
    {
     $user = $session->remove('user');

            return $this->render('base.html.twig');
    }

//AjouterClient 

#[Route('/joinus', name: 'inscrit', methods: ['GET', 'POST'])]
public function AddApprenant(Request $request, Security $security, EntityManagerInterface $entityManager,SessionInterface $session, UserPasswordEncoderInterface $passwordEncoder, EmailService $email): Response
{
    $user = new User();

    $user->setRole("Apprenant");
    $form = $this->createForm(UserType::class, $user);
    $form->handleRequest($request);
    
    if ($form->isSubmitted() && $form->isValid()) {
        $plainPassword = $form->get('password')->getData();
        $email->sendHelloEmail($user->getEmail(),$user->getNom());

        $hashedPassword = $passwordEncoder->encodePassword($user, $plainPassword);
        
        $user->setPassword($hashedPassword);
        $user->setConfirmerPassword($hashedPassword);

        // Enregistrer l'image
        $image = $form->get('image')->getData();
        if ($image) {
            $originalFilename = pathinfo($image->getClientOriginalName(), PATHINFO_FILENAME);
            $newFilename = $originalFilename.'.'.$image->guessExtension();
            try {
                $image->move(
                    $this->getParameter('image_directory'),
                    $newFilename
                );
            } catch (FileException $e) {
            }
            $user->setImage($newFilename);
        }
        
        $entityManager->persist($user);
        $entityManager->flush();
        $session->set('user', $user);

        
        return $this->render('client/profile.html.twig', [
            'user'=>$user
        ]);
    }
    $user = $security->getUser();

    return $this->render('InscriptionApprenant.html.twig', [
        'form' => $form->createView()
    ]);
}


    
     
//AffichageUsers
        #[Route('/users', name: 'list_users')]
        public function AffichageUser(UserRepository $repUser,SessionInterface $session)
        
        {
            $user1 = $session->get('user');
            if (!$user1 || ($user1 && $user1->getRole() !== 'Admin')) {
                return $this->redirectToRoute('login');
            }
            return $this->render("admin/TabUsers.html.twig", ["Users" => $repUser->findAll(),"user1"=>$user1,"user"=>$user1
        ]);
        }
//AffichageUser
        #[Route('/users/{id}', name: 'list_user')]
        public function AffichageUserById(UserRepository $repUser, $id,SessionInterface $session)
        {
            $user1 = $session->get('user');
            if (!$user1) {
                return $this->redirectToRoute('login');
            }
            $user = $repUser->find($id);
            return $this->render("editUser.html.twig", ["user" => $user]);
        }


//SUpprimerUser
 #[Route('/deleteUser/{id}', name: 'deleteUser')]
public function deleteUser($id, ManagerRegistry $manger, UserRepository $repo, Request $req,SessionInterface $session)
{
    $em = $manger->getManager();
    $user = $repo->find($id);
    $user1 = $session->get('user');
    if (!$user1 || ($user1 && $user1->getRole() !== 'Admin')) {
        return $this->redirectToRoute('login');
    }
    $em->remove($user);
    $em->flush();
    return new RedirectResponse($this->generateUrl('list_users'));
}

#[Route('/editUser/{id}', name: 'editUser')]
public function editUser($id, ManagerRegistry $manager, Request $request, UserRepository $repo)
{
    $em = $manager->getManager();
    $user = $repo->find($id);

    if (!$user) {
        throw $this->createNotFoundException('Utilisateur non trouvé');
    }
    $form = $this->createForm(UserType::class, $user);
    $form->get('password')->setData($user->getPassword());
    $form->get('confirmerPassword')->setData($user->getPassword());
    $form->add('password', PasswordType::class, [
        'disabled' => true, 
        'required' => false, 
        'attr' => [ 'style' => 'display:none' ] 
    ]);
    $form->add('confirmerPassword', PasswordType::class, [
        'disabled' => true,
        'required' => false,
        'attr' => [ 'style' => 'display:none' ] 
    
    ]);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
        // Mise à jour des données de l'utilisateur
        $user->setNom($form->get('nom')->getData());
        $user->setPrenom($form->get('prenom')->getData());
        $user->setEmail($form->get('email')->getData());
        
        // Traitement de l'image si elle est modifiée
        $image = $form->get('image')->getData();
        if ($image) {
            $originalFilename = pathinfo($image->getClientOriginalName(), PATHINFO_FILENAME);
            $newFilename = $originalFilename.'.'.$image->guessExtension();
            try {
                $image->move($this->getParameter('image_directory'), $newFilename);
                $user->setImage($newFilename);
            } catch (FileException $e) {
                // Gérer les exceptions de téléchargement de fichier
            }
        }
        $em->flush();

        return $this->redirectToRoute('list_users');
    }
    return $this->render('admin/editUser.html.twig', [
        'form' => $form->createView(),
        'user' => $user,
    ]);
}
//search
#[Route('/recherche', name:"app_admin_index", methods: ['GET'])]
public function index(Request $request, EntityManagerInterface $entityManager,SessionInterface $session): Response
{
    $nom = $request->query->get('nom');
    $userRepository = $entityManager->getRepository(User::class);
    $usersQuery = $userRepository->createQueryBuilder('u');

    if ($nom) {
        $usersQuery->andWhere('u.nom LIKE :nom');
        $usersQuery->setParameter('nom', '%' . $nom . '%');
    }
    $users = $usersQuery->getQuery()->getResult();

    return $this->render('admin/_user_table.html.twig', [
        'Users' => $users
    ]);
}


#[Route('/search', name: 'search', methods: ['GET'])]
public function search(Request $request, UserRepository $repo): JsonResponse
{
    $searchTerm = $request->query->get('searchTerm');
    $users = $repo->searchByName($searchTerm);
    $jsonData = [];
    foreach ($users as $user) {
        $userArray = [
            'id' => $user->getId(),
            'nom' => $user->getNom(),
            'prenom' => $user->getPrenom(),
            'email' => $user->getEmail(),
            'role' => $user->getRole(),
            'image' => $user->getImage() ?: 'Aucune image disponible', 
        ];
        $jsonData[] = $userArray;
    }
    return new JsonResponse($jsonData);
}

//tRIE 
#[Route('/tri', name:"app_admin1_index", methods: ['GET', 'POST'])]
public function index2(Request $request, SessionInterface $session,EntityManagerInterface $entityManager): Response
{
    $user1 = $session->get('user');
    $role = $request->query->get('role');

    $userRepository = $entityManager->getRepository(User::class);
    $usersQuery = $userRepository->createQueryBuilder('u');

    // Filtrage par rôle si un rôle est spécifié dans la requête
    if ($role) {
        $usersQuery->andWhere('u.role = :role');
        $usersQuery->setParameter('role', $role);
    }


    $users = $usersQuery->getQuery()->getResult();

    return $this->render('admin/TabUsers.html.twig', [
        'Users' => $users,
        "user1"=>$user1,
        "user"=>$user1

    ]);
}
   
   








 



//ADD USER PAR ADMIN 
//function generer password
function generateRandomPassword() {
    $randomNumber = mt_rand(100, 999);
    $characters = str_split('STUDY');
    $characters[] = $randomNumber;
    shuffle($characters);
    $password = implode('', $characters);
    return $password;
}
    #[Route('/gererUser', name: 'AddUser', methods: ['GET', 'POST'])]
    public function AddUser(Request $request, EntityManagerInterface $entityManager, SessionInterface $session, UserPasswordEncoderInterface $passwordEncoder, EmailService $email): Response
    {
        $user1 = $session->get('user');
        if (!$user1 || ($user1 && $user1->getRole() !== 'Admin')) {
            return $this->redirectToRoute('login');
        }
        
        $user = new User();
        $randomPassword = $this->generateRandomPassword();
        $user->setPassword($randomPassword);
        $user->setConfirmerPassword($randomPassword);
    
        $form = $this->createForm(AddUserType::class, $user);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $hashedPassword = $passwordEncoder->encodePassword($user, $user->getPassword());
            $user->setPassword($hashedPassword);
            $user->setConfirmerPassword($hashedPassword);
    
            $image = $form->get('image')->getData();
            if ($image) {
                $originalFilename = pathinfo($image->getClientOriginalName(), PATHINFO_FILENAME);
                $newFilename = $originalFilename . '.' . $image->guessExtension();
                try {
                    $image->move($this->getParameter('image_directory'), $newFilename);
                    $user->setImage($newFilename);
                } catch (FileException $e) {
                }
            }
    
            $entityManager->persist($user);
            $entityManager->flush();
            $email->sendInfoUser($user->getEmail(), $user, $randomPassword);
    
            return $this->redirectToRoute("list_users");
        }
    
        return $this->render('admin/gererUser.html.twig', [
            'form' => $form->createView(),
            'user' => $user1
        ]);
    }
//compte admin
#[Route('/dash', name: 'dash', methods: ['GET'])]
public function dash(SessionInterface $session): Response
{
    $user1 = $session->get('user');
    $user1 = $session->get('user');
    if (!$user1 || $user1->getRole() !== 'Admin') {
        return $this->redirectToRoute('login');
    }
    return $this->render('admin/admin.html.twig', [
        'user' => $user1,
    ]);   
}

    //Upggrate 
    function upgrateCompte(User $user,string $role) {
        $user->setRole($role);   
    }
    #[Route('/upgrade/{id}', name: 'upgrade', methods: ['GET'])]
    public function upgradeUserAccount(int $id, EntityManagerInterface $entityManager): Response
    {
        $user = $entityManager->getRepository(User::class)->find($id);
    
        // Vérifiez si l'utilisateur est un apprenant avant de procéder à la mise à niveau
        if ($user && $user->getRole() === 'Apprenant') {
            $this->upgrateCompte($user, 'Formateur');
            $entityManager->flush();
    
            $this->addFlash('success', 'Le compte de l\'apprenant a été mis à niveau avec succès.');
        } else {
            $this->addFlash('error', 'Impossible de mettre à niveau le compte. Le compte n\'est pas un apprenant.');
        }
    
        return $this->redirectToRoute('list_users');
    }
    

//validateRecaptcha
private function validateRecaptcha($recaptchaResponse): bool
{
    $secretKey = '6Lf5NsspAAAAAAXhauJw7QbRi41WvqTenjTKX2LS'; // Clé secrète reCAPTCHA

    // Construire les données pour la requête de validation
    $data = [
        'secret' => $secretKey,
        'response' => $recaptchaResponse,
    ];

    // Configurer les options de la requête HTTP
    $options = [
        'http' => [
            'header' => "Content-Type: application/x-www-form-urlencoded\r\n",
            'method' => 'POST',
            'content' => http_build_query($data),
        ],
    ];

    // Effectuer la requête vers l'API Google reCAPTCHA pour vérifier la réponse
    $url = 'https://www.google.com/recaptcha/api/siteverify';
    $context = stream_context_create($options);
    $result = file_get_contents($url, false, $context);

    // Vérifier si la réponse est récupérée avec succès
    if ($result === false) {
        // Gérer l'erreur de récupération de la réponse
        return false;
    }

    // Analyser la réponse JSON de l'API reCAPTCHA
    $response = json_decode($result);

    // Vérifier si la réponse JSON est correctement analysée
    if ($response === null || !isset($response->success)) {
        // Gérer l'erreur d'analyse de la réponse JSON
        return false;
    }

    // Retourner true si la réponse est valide (success = true)
    return $response->success;
}

}