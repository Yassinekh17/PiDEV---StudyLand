<?php

namespace App\Controller;

use App\Repository\UserRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Cookie;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\HttpFoundation\JsonResponse;



class CookieController extends AbstractController
{
    #[Route('/jeux', name: "jeux")]
    public function game(Request $request, SessionInterface $session, UserRepository $userRepository): Response
    {
        // Récupérer l'utilisateur de la session
        $user = $session->get('user');
        
        if (!$user) {
            // Rediriger l'utilisateur vers la page de connexion s'il n'est pas connecté
            return $this->redirectToRoute('connexion');
        }
        
        // Vérifier si un cookie de session de jeu existe pour l'utilisateur pour la journée en cours
        $gameSession = $request->cookies->get('game_session_' . $user->getId() . '_' . date('Y-m-d'));
    
        if ($gameSession === 'started') {
            // L'utilisateur a déjà commencé une session de jeu pour la journée en cours
            return new Response('Vous avez déjà commencé une session de jeu aujourd\'hui.');
        } else {
            // Créer un cookie pour indiquer que l'utilisateur a commencé une session de jeu pour la journée en cours
            $cookieValue = 'started';
            $cookie = Cookie::create('game_session_' . $user->getId() . '_' . date('Y-m-d'), $cookieValue, strtotime('tomorrow'));
    
            // Créer une réponse avec le cookie
            $response = new Response();
            $response->headers->setCookie($cookie);
           
    
            // Rendre le jeu pour l'utilisateur
            return $this->render('admin/test.html.twig', ['user' => $user]);
        }
    }
  #[Route('/increment-bonus/{id}', name: 'increment_bonus')]
public function incrementBonus($id, UserRepository $userRepository): Response
{
    // Récupérer l'utilisateur en fonction de son ID
    $user = $userRepository->find($id);

    if (!$user) {
        // Gérer le cas où l'utilisateur n'est pas trouvé
        // Par exemple, renvoyer une erreur 404
        throw $this->createNotFoundException('Utilisateur non trouvé');
    }

    // Incrémenter le bonus de l'utilisateur
    $user->setBonus($user->getBonus() + 1);

    // Enregistrer les modifications de l'utilisateur
    $entityManager = $this->getDoctrine()->getManager();
    $entityManager->persist($user);
    $entityManager->flush();

    // Répondre avec un message JSON pour indiquer que le bonus a été incrémenté avec succès
    return $this->json(['message' => 'Bonus incrémenté avec succès']);
}
}

