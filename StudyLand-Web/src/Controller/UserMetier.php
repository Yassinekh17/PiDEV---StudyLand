<?php

namespace App\Controller;
use Symfony\Component\Security\Core\Exception\AuthenticationException;
use App\Entity\User;
use Symfony\Component\HttpFoundation\RedirectResponse;
use App\Form\AddUserType;
use App\Form\EditUserType;
use App\Form\UserType;
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
use App\Service\EmailService;
use Dompdf\Dompdf;
use Dompdf\Options;
use App\Form\LoginFormType;
use PharIo\Manifest\Email;
use PhpParser\Node\Expr\BinaryOp\Equal;
use Symfony\Component\HttpClient\Exception\RedirectionException;
use Symfony\Component\Security\Core\Authorization\Voter\AuthenticatedVoter;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;

class UserMetier extends AbstractController
{


//search user


    //tri par Role
    

//mdp   oublier
private function generateCode(): string
{
    return str_pad(rand(0, 9999), 4, '0', STR_PAD_LEFT);
}
    #[Route('/login', name: 'login', methods: ['GET', 'POST'])]
public function login(Request $request, AuthenticationUtils $authenticationUtils, UserPasswordEncoderInterface $passwordEncoder, Security $security,SessionInterface $session): Response
{
    $error = $authenticationUtils->getLastAuthenticationError();
    $lastUsername = $authenticationUtils->getLastUsername();
    if ($request->isMethod('POST')) {
        $email = $request->request->get('email');
        $password = $request->request->get('password');
        $user = $this->getDoctrine()->getRepository(User::class)->findOneBy(['email' => $email]);
        
        if (!$user) {
            $error = 'Email ou mot de passe incorrect';
        } elseif (!$passwordEncoder->isPasswordValid($user, $password)) {
            $error = 'Email ou mot de passe incorrect';
        } else {
            $session->set('user', $user);

            if ($user->getRole() === 'Apprenant') {
                 return $this->render('client/profile.html.twig', [
                'user'=>$user]);} 
            elseif ($user->getRole() === 'Admin') {
                return $this->redirectToRoute('dash');
            }
            else{
                return $this->render('client/profileFormateur.html.twig', [
                    'user'=>$user]);}
            

            return $this->redirectToRoute('home');
        }
    }
    $user = $security->getUser();
    return $this->render('login.html.twig', [
        'error' => $error,
        'last_username' => $lastUsername,
        'user' => $user,
    ]);
}



#[Route('/sendCode/{id}', name: 'sendCode', methods: ['GET', 'POST'])]
public function sendCode($id, Request $request, EmailService $emailService, UserRepository $repo, SessionInterface $session)
{
    $code = $this->generateCode();
    $user = $repo->find($id);
    $email = $user->getEmail();
    $emailService->sendCodeMdp($email, $code);
    $session->set('code', $code); 
    $code1= $code;
    
    return $this->render('client\SaisirCode.html.twig', [
        'user' => $user,
        'code1' => $code1 // Passer le code à la vue pour affichage ou comparaison
    ]);
}


#[Route('/securiteClient/{id}', name: 'securiteClient', methods: ['GET', 'POST'])]
public function securiteClient($id, Request $request, UserRepository $repo, EntityManagerInterface $entityManager, UserPasswordEncoderInterface $passwordEncoder, EmailService $emailService, SessionInterface $session): Response
    {
        
        $user=$repo->find($id);
        $code = $this->generateCode();
        // Si l'email n'a pas été envoyé, afficher le formulaire sur la même page
        return $this->render('client\VerifierCode.html.twig', [
            'user' => $user,
        ]);
    }
    #[Route('/verify-code', name: 'verify_code', methods: ['POST'])]
    public function verifyCode(Request $request, SessionInterface $session): Response
    {
        $sessionCode = $session->get('code');
        $enteredCode = $request->request->get('code1');
        if ($enteredCode == $sessionCode) {
            $response = ['status' => 'error', 'message' => 'Le code est incorrect.'];
        } else {
            $response = ['status' => 'success', 'message' => 'Le code est correct.'];

        }
        return $this->json($response);
    }
    
    #[Route('/update-password/{id}', name: 'update_password', methods: ['POST'])]
    public function updatePassword($id, Request $request, UserRepository $repo,  EntityManagerInterface  $manager,UserPasswordEncoderInterface $passwordEncoder): Response
    {
        // Récupérer l'utilisateur correspondant à l'ID
        
        $user = $repo->find($id);
        if (!$user) {
            // Gérer le cas où aucun utilisateur n'est trouvé avec l'ID donné
            $response = ['status' => 'error', 'message' => 'Utilisateur non trouvé.'];
            return $this->json($response);
        }
    
        // Récupérer le nouveau mot de passe depuis la requête
        $newPassword = $request->request->get('newPassword');
    
        // Valider le nouveau mot de passe (vous pouvez ajouter des validations supplémentaires ici)
    
        // Encoder le nouveau mot de passe
        $hashedPassword = $passwordEncoder->encodePassword($user, $newPassword);
    
        // Mettre à jour le mot de passe de l'utilisateur
        $user->setPassword($hashedPassword);
    
        // Enregistrer les modifications dans la base de données
        $manager->persist($user);
        $manager->flush();
    
        // Répondre avec un message de succès
        $response = ['status' => 'success', 'message' => $id];
        return $this->json($response);
    }





public function index2(): Response
{
    return $this->render('admin/statstiqueUser.html.twig');
}



#[Route('/state', name: "state")]
public function userStats(UserRepository $userRepository,SessionInterface $session): Response
    {
        $userStats = $userRepository->countUsersByRole();
        // Transformer les données en un format adapté pour le graphique
        $labels = [];
        $data = [];

        foreach ($userStats as $stat) {
            $labels[] = $stat['role'];
            $data[] = $stat['userCount'];
        }
        $user1 = $session->get('user');
        if (!$user1 || ($user1 && $user1->getRole() !== 'Admin')) {
            return $this->redirectToRoute('login');
        }
        return $this->render('admin/statstiqueUser.html.twig', [
            'labels' => json_encode($labels),
            'data' => json_encode($data),
            'user'=>$user1,
            
        ]);
    }




















    
/**
    * @Route("/user/pdf", name="pdf_Users", methods={"GET"})
    */
    public function pdf(UserRepository $repo): Response
    {
        $User = $repo->findAll();
 
        $pdfOptions = new Options();
        $pdfOptions->set('defaultFont', 'Arial');
 
        $dompdf = new Dompdf($pdfOptions);
 
        $html = $this->renderView('admin/pdf.html.twig', [
            'users' => $User, 
        ]);
 
        $dompdf->loadHtml($html);
 
        $dompdf->setPaper('A4', 'portrait');
 
        // Render the HTML as PDF
        $dompdf->render();
 
        // Output the generated PDF to Browser (inline view)
        return new Response($dompdf->output(), Response::HTTP_OK, [
            'Content-Type' => 'application/pdf',
        ]);
    }



    






}