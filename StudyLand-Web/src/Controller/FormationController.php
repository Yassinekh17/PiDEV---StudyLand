<?php

namespace App\Controller;

use App\Entity\Categorie;
use App\Entity\Formation;
use App\Form\FormationType;
use App\Repository\CategorieRepository;
use App\Repository\FormationRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\Persistence\ManagerRegistry;
use Laminas\Code\Generator\EnumGenerator\Name;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\Transport\Smtp\EsmtpTransport;


class FormationController extends AbstractController
{   #[Route('/', name: "home")]
    public function index(): Response
    {
        return $this->render('base.html.twig');
    }
    #[Route('/AddFormation', name: 'AddFormation', methods: ['GET', 'POST'])]
    public function addFormation(Request $request, EntityManagerInterface $entityManager): Response
    {
        $formation = new Formation();
        
        $form = $this->createForm(FormationType::class, $formation);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($formation);
            $entityManager->flush();
            $this->sendEmail("Formation ajoutée avec succees","!!");

            return $this->redirectToRoute("list_Formation");
        }
    
        return $this->render('AjouterFormation.html.twig', [
            'form' => $form->createView(),
        ]);
    }
        #[Route('/Formation', name: 'list_Formation')]
        public function AffichageFormation(FormationRepository $repFormation)
        {
            return $this->render("TabFormation.html.twig", ["Formation" => $repFormation->findAll()]);
        }
        #[Route('/deletef/{idFormation}', name: 'deletef')]
    public function delete($idFormation, ManagerRegistry $manger, FormationRepository $repo, Request $req)
    {
        $em = $manger->getManager();
        $formation = $repo->find($idFormation);
        $em->remove($formation);
        $em->flush();
        
            
        
        return $this->redirectToRoute('list_Formation');


    }

    #[Route('/editf/{idFormation}', name: 'editf')]
    public function editAuthorAction($idFormation, ManagerRegistry $manager, Request $request, FormationRepository $repo)
    {
        $em = $manager->getManager();
        $formation = $repo->find($idFormation);
        $form = $this->createForm(FormationType::class, $formation);
        $form->handleRequest($request);
        if ($form->isSubmitted()) {
            $em->flush();
            return $this->redirectToRoute('list_Formation');
        }
        return $this->render('editFormation.html.twig', [
            'form' => $form->createView(), 'Formation' => $formation,
        ]);
    }
    
    #[Route('/FormationFront', name: 'list_FormationF')]
    public function AffichageFormationFront(FormationRepository $repFormation, PaginatorInterface $paginator, Request $request)
    {
        // Retrieve all formations query
        $query = $repFormation->createQueryBuilder('f')->getQuery();
    
        // Paginate the results
        $formations = $paginator->paginate(
            $query, // Query to paginate
            $request->query->getInt('page', 1), // Current page number, default is 1
            3 // Items per page
        );
    
        return $this->render("course.html.twig", ["Formation" => $formations]);
    }

    #[Route('/searchByNomFormation', name: 'searchByNomFormation')]
    public function searchByNomFormation(Request $request, FormationRepository $formationRepository): Response
    {
        $searchTerm = $request->query->get('search');
        $formations = $formationRepository->createQueryBuilder('f')
            ->where('f.titre LIKE :searchTerm')
            ->setParameter('searchTerm', '%' . $searchTerm . '%')
            ->getQuery()
            ->getResult();
    
        return $this->render('TabFormation.html.twig', [
            'Formation' => $formations,
            'tri' => null, // Add the 'tri' variable to the context with a default value
        ]);
    }

    #[Route('/triFormation', name: 'triFormation')]
    public function triFormation(Request $request, FormationRepository $formationRepository): Response
    {
        $critere = $request->query->get('critere');
    
        // Default criterion to 'titre' if not provided or invalid
        if (!in_array($critere, ['titre', 'datedebut'])) {
            $critere = 'titre';
        }
    
        // Sort formations based on the selected criterion
        $formations = $formationRepository->findBy([], [$critere => 'ASC']);
    
        return $this->render('TabFormation.html.twig', [
            'Formation' => $formations,
            'tri' => $critere,
        ]);
    }
    #[Route('/formation-statistics', name: 'formation_statistics')]
    public function formationStatistics(FormationRepository $formationRepository): Response
{
    // Retrieve the formations from the database
    $formations = $formationRepository->findAll();

    // Prepare data for the chart
    $formationCounts = [];
    foreach ($formations as $formation) {
        $categoryName = $formation->getNomCategorie()->getNomCategorie(); // Get the category name
        if (!isset($formationCounts[$categoryName])) {
            $formationCounts[$categoryName] = 0; // Initialize the count for this category
        }
        $formationCounts[$categoryName]++; // Increment the count for this category
    }

    // Convert data into the format required for Chart.js
    $labels = array_keys($formationCounts);
    $data = array_values($formationCounts);

    // Return JSON response containing data for the chart
    return new JsonResponse([
        'labels' => $labels,
        'data' => $data,
    ]);
}

    #[Route('/countprixformation', name: 'countprixformation')]
    public function countprixformation(FormationRepository $formationRepository): Response
    {
        // Récupérez les formations depuis la base de données
        $formations = $formationRepository->findAll();

        // Calculez le total des prix des formations
        $totalPrice = 0;
        foreach ($formations as $formation) {
            $totalPrice += $formation->getPrix();
        }

        // Déterminez le pourcentage par rapport à un montant total (par exemple, 1000)
        $totalAmount = 10000; // Montant total de référence 
        $percentage = ($totalPrice / $totalAmount) * 100;

        // Retournez la réponse avec les valeurs calculées
        return $this->render('admin/admin.html.twig', [
            'totalPrice' => $totalPrice,
            'percentage' => $percentage,
        ]);
    }
    function sendEmail($subject, $message)
    {
        $transport = new EsmtpTransport('smtp.gmail.com', 587);
        $transport->setUsername("mohamedsalah.bedoui@esprit.tn");
        $transport->setPassword("rmupskxoevczbone");
    
        $mailer = new Mailer($transport);
    
        $email = (new Email())
            ->from("pidev@gmail.com")
            ->to("mohamedyassine.khiari@esprit.tn")
            ->subject($subject)
            ->text($message);
    
        $mailer->send($email);
    }

    #[Route('/', name: "homeFC")]
    public function indexFC(): Response
    {
      // Fetch formations from the database
      $formations = $this->getDoctrine()->getRepository(Formation::class)->findAll();

      // Fetch categories from the database
$categories = $this->getDoctrine()->getRepository(Categorie::class)->findAll();

// Pass formations and categories to the template
return $this->render('base.html.twig', [
    'Formation' => $formations,
    'Categorie' => $categories, // Pass the categories variable to the template
]);

  }
  #[Route('/dashFC', name: "dashFC")]
  public function indexAdmin(FormationRepository $formationRepository): Response
  { 
    // Récupérez les formations depuis la base de données
    $formations = $formationRepository->findAll();

    // Calculez le total des prix des formations
    $totalPrice = 0;
    foreach ($formations as $formation) {
        $totalPrice += $formation->getPrix();
    }

    // Déterminez le pourcentage par rapport à un montant total (par exemple, 1000)
    $totalAmount = 10000; // Montant total de référence (vous devez définir cette valeur correctement)
    $percentage = ($totalPrice / $totalAmount) * 100;
    return $this->render('admin/admin.html.twig', [
      'totalPrice' => $totalPrice,
      'percentage' => $percentage,'%'
  ]);
}
}


