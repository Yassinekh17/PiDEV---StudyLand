<?php

namespace App\Controller;

use App\Entity\Categorie;
use App\Form\CategorieType;
use App\Repository\CategorieRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\Persistence\ManagerRegistry;
use Laminas\Code\Generator\EnumGenerator\Name;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

class CategorieController extends AbstractController
{   #[Route('/', name: "home")]
    public function index(): Response
    {
        return $this->render('base.html.twig');
    }
    #[Route('/AddCategorie', name: 'AddCategorie', methods: ['GET', 'POST'])]
    public function AddCategorie(Request $request,SessionInterface $session, EntityManagerInterface $entityManager): Response
    {
        $categorie = new Categorie();
        $form = $this->createForm(CategorieType::class, $categorie);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            // Affecter la valeur saisie dans le champ nomCategory à la propriété category de l'entité
            $categorie->setNomCategorie($form->get('nomCategorie')->getData());
    
            // Enregistrement de la catégorie dans la base de données
            $entityManager->persist($categorie);
            $entityManager->flush();
    
            return $this->redirectToRoute('list_Categorie');
        }
        $user = $session->get('user');
        return $this->render('AjouterCategorie.html.twig', [
            'form' => $form->createView(),
            'user'=>$user
        ]);
    }
    

        #[Route('/Categorie', name: 'list_Categorie')]
        public function AffichageCategorie(CategorieRepository $repCategorie)
        {
            return $this->render("TabCategorie.html.twig", ["Categorie" => $repCategorie->findAll()]);
        }
        #[Route('/delete/{idCategorie}', name: 'deleteCat')]
        public function delete($idCategorie, ManagerRegistry $manager, CategorieRepository $repo, Request $request): Response
        { 
            $em = $manager->getManager();
            
            // Utilize the identifier passed in the URL to find the category to delete
            $categorie = $repo->findOneBy(['idCategorie' => $idCategorie]);
            
            // Check if the category exists
            if (!$categorie) {
                // Handle the case where the category is not found
                return new Response('Category not found', Response::HTTP_NOT_FOUND);
            }
        
            // Remove the category from the database
            $em->remove($categorie);
            $em->flush();
            
            return $this->redirectToRoute('list_Categorie');
        }

        #[Route('/edit/{idCategorie}', name: 'editCat')]
        public function editAuthorAction($idCategorie, ManagerRegistry $manager, Request $request, CategorieRepository $repo)
        {
            $em = $manager->getManager();
            $categorie = $repo->findOneBy(['idCategorie' => $idCategorie]);
            $form = $this->createForm(CategorieType::class, $categorie);
            $form->handleRequest($request);
            if ($form->isSubmitted()) {
                $em->flush();
                return $this->redirectToRoute('list_Categorie');
            }
            return $this->render('editCategorie.html.twig', [
                'form' => $form->createView(), 'categorie' => $categorie,
            ]);
        }
        #[Route('/searchByNomCategorie', name: 'searchByNomCategorie')]
        public function searchByNomCategorie(Request $request, CategorieRepository $categorieRepository): Response
        {
            $searchTerm = $request->query->get('search');
            $categories = $categorieRepository->createQueryBuilder('c')
                ->where('c.nomCategorie LIKE :searchTerm')
                ->setParameter('searchTerm', '%' . $searchTerm . '%')
                ->getQuery()
                ->getResult();
        
            return $this->render('TabCategorie.html.twig', [
                'Categorie' => $categories,
                'tri' => null, // Add the 'tri' variable to the context with a default value
            ]);
        }
    
        #[Route('/triCategorie/{critere}', name: 'triCategorie')]
        public function triCategorie($critere, CategorieRepository $categorieRepository): Response
        {
            // Implement sorting logic based on the selected criterion ($critere)
            $categories = $categorieRepository->findBy([], ['nomCategorie' => $critere]);
    
            return $this->render('TabCategorie.html.twig', [
                'Categorie' => $categories,
                'tri' => $critere, // Pass the sorting criterion to the template
            ]);
        }
       
    }
          
        



 