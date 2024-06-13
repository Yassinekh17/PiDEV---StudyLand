<?php

namespace App\Controller;

use App\Entity\CourFormation;
use App\Entity\YoutubeService;

use App\Entity\Formation;
use App\Form\CourFormationType;
use App\Repository\CourFormationRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\Persistence\ManagerRegistry;
use Laminas\Code\Generator\EnumGenerator\Name;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Knp\Component\Pager\PaginatorInterface;
use Doctrine\ORM\Tools\Pagination\Paginator;





class CourFormationController extends AbstractController
{   #[Route('/', name: "home")]
    public function index(): Response
    {
        return $this->render('base.html.twig');
    }
    #[Route('/AddCourFormation', name: 'AddCourFormation', methods: ['GET', 'POST'])]
    public function AddCourFormation(Request $request, EntityManagerInterface $entityManager): Response
    {
        $cour = new CourFormation();
        $form = $this->createForm(CourFormationType::class, $cour);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // Handle the file upload
            $file = $form['descriptionCours']->getData();
            // Check if a file was uploaded
            if ($file) {
                $fileName = md5(uniqid()) . '.' . $file->guessExtension();
    
                // Move the file to the desired directory
                $file->move(
                    $this->getParameter('description_cours_directory'), // Define this parameter in services.yaml
                    $fileName
                );
    
                // Set the file name to the entity
                $cour->setDescriptionCours($fileName);
            }
    
            // Set the idformation based on the selected Formation
            $selectedFormation = $form->get('idformation')->getData();
            $cour->setIdformation($selectedFormation); // Set the Formation entity, not just its ID
    
            // Persist the CourFormation entity
            $entityManager->persist($cour);
            $entityManager->flush();
    
            // Redirect to home route
            return $this->redirectToRoute("list_Cours");
        }
    
        return $this->render('AjouterCourFormation.html.twig', [
            'form' => $form->createView()
        ]);
    }
  

    #[Route('/Cours', name: 'list_Cours', methods: ['GET'])]
    public function listCours(CourFormationRepository $courFormationRepository): Response
    {
        // Fetch all courses from the repository
        $courses = $courFormationRepository->findAll();
        
        // Render the template and pass the courses to it
        return $this->render('ListeCoursFormation.html.twig', [
            'courses' => $courses,
        ]);
    }
    
    #[Route('/download-pdf/{idcour}', name: 'download_pdf')]
    public function downloadPdfAction($idcour, CourFormationRepository $courFormationRepository): Response
    {
        // Retrieve the CourFormation entity from the repository based on the provided ID
        $cour = $courFormationRepository->find($idcour);
    
        // Check if the CourFormation entity exists
        if (!$cour) {
            throw $this->createNotFoundException('The course with id ' . $idcour . ' does not exist.');
        }
    
        // Get the file content (assuming it's stored as a BLOB in the database)
        $fileContent = $cour->getDescriptionCours();
    
        // Generate a temporary file path to store the PDF content
        $tempFilePath = tempnam(sys_get_temp_dir(), 'pdf');
    
        // Write the PDF content to the temporary file
        file_put_contents($tempFilePath, $fileContent);
    
        // Create a BinaryFileResponse to send the PDF file to the user
        $response = new BinaryFileResponse($tempFilePath);
    
        // Set the response headers to indicate that it's a PDF file and force download
        $response->setContentDisposition(
            ResponseHeaderBag::DISPOSITION_ATTACHMENT,
            'downloaded_file.pdf' // Set the file name here
        );
    
        // Set the content type as application/pdf
        $response->headers->set('Content-Type', 'application/pdf');
    
        // Delete the temporary file after the response is sent
        $response->deleteFileAfterSend(true);
    
        return $response;
    }
    #[Route('/deletec/{idcour}', name: 'deletec')]
    public function delete($idcour, ManagerRegistry $manager, CourFormationRepository $repo, Request $request): Response
    { 
        $em = $manager->getManager();
        
        // Utilize the identifier passed in the URL to find the category to delete
        $cours = $repo->findOneBy(['idcour' => $idcour]);
        
        // Check if the category exists
        if (!$cours) {
            // Handle the case where the category is not found
            return new Response('cours not found', Response::HTTP_NOT_FOUND);
        }
    
        // Remove the category from the database
        $em->remove($cours);
        $em->flush();
        
        return $this->redirectToRoute('list_Cours');
    }
    #[Route('/editc/{idcour}', name: 'editc', methods: ['GET', 'POST'])]
    public function editCoursAction($idcour, Request $request, EntityManagerInterface $entityManager, CourFormationRepository $repo): Response
    {
        // Find the course entity by its ID
        $cours = $repo->findOneBy(['idcour' => $idcour]);
    
        // Check if the course exists
        if (!$cours) {
            // Handle the case where the course is not found
            throw $this->createNotFoundException('The course with id ' . $idcour . ' does not exist.');
        }
    
        // Create the form using the CourFormationType form type
        $form = $this->createForm(CourFormationType::class, $cours);
    
        // Handle the form submission
        $form->handleRequest($request);
    
        // Check if the form is submitted and valid
        if ($form->isSubmitted() && $form->isValid()) {
            // Persist the changes to the database
            $entityManager->flush();
    
            // Redirect to the list of courses
            return $this->redirectToRoute('list_Cours');
        }
    
        // Render the edit form template with the form and course entity
        return $this->render('editCourFormation.html.twig', [
            'form' => $form->createView(),
            'cours' => $cours,
        ]);
    }

   
    #[Route('/coursFront', name: 'list_CoursF')]
    public function AffichageCourFront(CourFormationRepository $courFormationRepository, PaginatorInterface $paginator, \App\Service\YouTubeService $youtubeService, Request $request): Response
    {
        // Retrieve all formations query
        $query = $courFormationRepository->createQueryBuilder('f')
            ->getQuery();
        
        // Count total number of items
        $totalItems = $courFormationRepository->count([]);
        
        // Paginate the results
        $courFormations = $paginator->paginate(
            $query, // Query to paginate
            $request->query->getInt('page', 1), // Current page number, default is 1
            3 // Items per page
        );
        
        return $this->render("cours.html.twig", ["CourFormation" => $courFormations]);
    }

#[Route('/searchByNomCour', name: 'searchByNomCour')]
public function searchByNomCategorie(Request $request, CourFormationRepository $courRepository): Response
{
    // Check if it's an AJAX request
    if ($request->isXmlHttpRequest()) {
        $searchTerm = $request->query->get('search');
        $cours = $courRepository->createQueryBuilder('c')
            ->where('c.nomCours LIKE :searchTerm')
            ->setParameter('searchTerm', '%' . $searchTerm . '%')
            ->getQuery()
            ->getResult();

        // Prepare the data to be sent back as JSON
        $responseData = [];
        foreach ($cours as $course) {
            $responseData[] = [
                // 'IdCour' => $course->getIdcour(),
                'nomCours' => $course->getNomCours(),
                
            ];
        }

        // Return the search results as JSON response
        return new JsonResponse($responseData);
    }

    // If it's not an AJAX request, retrieve the courses and render the template
    $cours = $courRepository->findAll();
    return $this->render('ListeCoursFormation.html.twig', [
        'courses' => $cours,
    ]);
}


    
   
#[Route('/Youtube', name: 'Youtube')]
public function youtubeapi(\App\Service\YouTubeService $youtubeService, CourFormationRepository $courRepository): Response
{
    // Example usage: searching for videos with a query
    $apiKey = 'AIzaSyDgSEQuU80tbiip3BuHVpRbGWVIXqWJ1Dw'; // Replace with your actual API key
    $response = $youtubeService->searchVideos('java', $apiKey);

    // Fetch CourFormation data from the repository
    $courFormationData = $courRepository->findAll();

    // Process the response and use the data in your application

    return $this->render('cours.html.twig', [
        'CourFormation' => $courFormationData,
        'response' => $response,
    ]);
}

#[Route('/cour-statistics', name: 'cour_statistics')]
public function courStatistics(CourFormationRepository $courRepository): Response
{
    // Retrieve the courses from the database
    $cours = $courRepository->findAll();

    // Prepare data for the chart
    $coursCounts = [];
    foreach ($cours as $cour) {
        $categoryName = $cour->getIdformation()->getTitre(); // Get the category name
        if (!isset($coursCounts[$categoryName])) {
            $coursCounts[$categoryName] = 0; // Initialize the count for this category
        }
        $coursCounts[$categoryName]++; // Increment the count for this category
    }

    // Prepare the data to be returned as JSON
    $responseData = [
        'labels' => array_keys($coursCounts),
        'data' => array_values($coursCounts),
    ];

    // Return the response as JSON
    return new JsonResponse($responseData);
}


}