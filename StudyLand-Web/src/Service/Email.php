<?php

namespace App\Service;

use App\Entity\User;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;

class EmailService
{
    private $mailer;

    public function __construct(MailerInterface $mailer)
    {
        $this->mailer = $mailer;
    }

    public function sendHelloEmail(string $recipientEmail, string $Nom)
    {
        $imagePath = '../logo.png';

        $email = (new Email())
            ->from('studyland002@gmail.com')
            ->to($recipientEmail)
            ->subject('Bienvenue sur notre plateforme!')
            ->html('<html>' .
                '<body>' .
                '<p>Cher ' . $Nom . ',</p>' .
                '<p><strong>Merci de vous être inscrit à notre plateforme.</strong></p>' .
                '<p>Cordialement,<br>StudyLand</p>' .
                '<img src="' . $imagePath . '" alt="Logo">' . 
                '</body>' .
                '</html>'
            );

        $this->mailer->send($email);
    }





public function sendCodeMdp(string $recipientEmail, String $Code)
{
    $email = (new Email())
    ->from('studyland002@gmail.com')
    ->to($recipientEmail)
    ->subject('Code de vérification')
    ->html('<html>' .
        '<body>' .
        '<p>Vous avez demandé un code de vérification pour réinitialiser votre mot de passe.</p>' .
        '<p>Voici votre code de vérification : <strong>' . $Code. '</strong></p>' .
        '<p>Si vous n\'avez pas demandé de réinitialisation de mot de passe, vous pouvez ignorer cet e-mail.</p>' .
        '<p>Cordialement,<br>StudyLand</p>' .
        '</body>' .
        '</html>'
    );
$this->mailer->send($email);
}

public function sendInfoUser(string $recipientEmail, User $user,string $randomPassword)
{
    $email = (new Email())
        ->from('studyland002@gmail.com')
        ->to($recipientEmail)
        ->subject('Code de vérification')
        ->html('<html>' .
            '<body>' .
            '<p>Cher ' . $user->getNom() . ',</p>' .
            '<p>Nous avons le plaisir de vous informer que vous avez été admis à StudyLand en tant que' .'  '. $user->getRole().'</p>' .
            '<p><strong>Informations d inscription à renseigner :</strong></p>' .
            ' <p>Votre email : ' . $user->getEmail() . ' </p>' .
            ' <p>Votre mot de passe  : ' . $randomPassword. ' </p>' .

            '<p>Cordialement,<br>StudyLand</p>' .
            '</body>' .
            '</html>'
        );
    $this->mailer->send($email);
}


}
