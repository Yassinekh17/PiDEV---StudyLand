<?php
// src/Controller/FeedbackController.php
namespace App\Controller;
use App\Entity\Feedback;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;

class FeedbackController extends AbstractController
{
    #[Route('/insert-feedback', name: 'insert_feedback', methods: ['GET', 'POST'])]
     
    
    public function insertFeedback(Request $request): Response
    {
        // Connexion à la base de données
        $servername = "localhost";
        $username = "root";
        $password = "";
        $dbname = "studyland";

        $conn = new \mysqli($servername, $username, $password, $dbname);

        // Vérifier la connexion
        if ($conn->connect_error) {
            die("Échec de la connexion : " . $conn->connect_error);
        }

        // Récupérer les données du formulaire
      // Récupérer les données du formulaire
// Récupérer les données du formulaire
$nom = $request->request->get('nom');
$email = $request->request->get('email');
$opinion = $request->request->get('opinion');
$methodes_enseignement = isset($_POST["methodes_enseignement"]) ? implode(", ", $_POST["methodes_enseignement"]) : "";
$evaluation_recherche = isset($_POST["evaluation_recherche"]) ? $_POST["evaluation_recherche"] : "";



        // Préparer et exécuter la requête SQL
        $sql = "INSERT INTO feedback (nom, email, opinion, methodes_enseignement, evaluation_recherche)
            VALUES ('$nom', '$email', '$opinion', '$methodes_enseignement', '$evaluation_recherche')";

if ($conn->query($sql) === TRUE) {
    $message = "Feedback successfully added.";
    
    // Add flash message
    $this->addFlash('success', $message);
    
    // Close the database connection
    $conn->close();
    
    // Redirect to another route where the message will be displayed
    return $this->redirectToRoute('list_CoursF');
} else {
    $message = "Erreur : " . $sql . "<br>" . $conn->error;
}

        // Fermer la connexion à la base de données
        $conn->close();

        return $this->render('surv.html.twig', [
            'message' => $message,
        ]);
    }

    
    
    
    
}