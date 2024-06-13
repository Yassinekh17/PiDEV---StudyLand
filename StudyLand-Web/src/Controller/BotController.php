<?php

namespace App\Controller;

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;

class BotController extends AbstractController
{
    #[Route('/Aide', name: 'app_bot')]
    public function index(Request $request, NormalizerInterface $normalizer): Response
    {
        $qa = [
            'bonjour' => 'Bonjour ! Comment puis-je vous aider ?',
            'Recherche de cours' => 'Vous pouvez trouver des cours en utilisant notre barre de recherche. Entrez des mots-clés pertinents pour trouver les cours qui vous intéressent.',
            'Cours en ligne' => 'Nos cours en ligne sont disponibles sur notre plateforme. Vous pouvez parcourir notre catalogue pour trouver des cours dans différents domaines.',
            'formation gratuite' => 'Nous proposons également des formations gratuites. Vous pouvez les trouver en filtrant les résultats de recherche par prix.',
            'comment' => 'Je suis là pour répondre à vos questions. Que souhaitez-vous savoir ?',
            'Problème de paiement' => 'Si vous rencontrez des problèmes de paiement, veuillez contacter notre service clientèle à l\'adresse support@example.com pour obtenir de l\'aide.',
            'Inscription à un cours' => 'Pour vous inscrire à un cours, il vous suffit de trouver le cours qui vous intéresse et de suivre les instructions d\'inscription sur la page du cours.',
            'Assistance technique' => 'Si vous avez besoin d\'assistance technique, veuillez contacter notre équipe d\'assistance à l\'adresse support@example.com. Nous serons heureux de vous aider.',
            'Options de paiement' => 'Nous acceptons plusieurs méthodes de paiement, y compris les cartes de crédit, les virements bancaires et les portefeuilles électroniques. Vous pouvez choisir la méthode qui vous convient le mieux lors du paiement.',
            'Certificat ' => 'Certains de nos cours offrent des certificats à la fin. Vous pouvez vérifier si un cours propose un certificat sur la page du cours.',
            // Ajoutez d'autres questions et réponses ici...
        ];

        // Récupérer le message envoyé par l'utilisateur
        $message = $request->request->get('message');

        // Initialiser la réponse par défaut
        $response = 'Bonjour ! Comment puis-je vous aider ?';

        // Vérifier si la question de l'utilisateur correspond à une réponse préenregistrée
        foreach ($qa as $question => $answer) {
            if (stripos($message, $question) !== false) {
                $response = $answer;
                break;
            }
        }

        // Afficher les questions similaires
        $similarQuestions = array_keys($qa, $response);

        // Rendre la vue avec la réponse et les questions similaires
        return $this->render('bot/index.html.twig', [
            'response' => $response,
            'similarQuestions' => $similarQuestions,
        ]);
    }

    #[Route('/correct-spelling', name: 'app_correct_spelling')]
    public function correctSpelling(Request $request, NormalizerInterface $normalizer): Response
    {
        $question = $request->request->get('question');

        // Vous pouvez utiliser une bibliothèque de correction orthographique, comme Symfony/String
        // Exemple : normalisation du texte
        $correctedQuestion = $normalizer->normalize($question);

        // Pour l'instant, nous renvoyons simplement la question corrigée en guise de démonstration
        return new JsonResponse(['suggestions' => $correctedQuestion]);
    }
}
