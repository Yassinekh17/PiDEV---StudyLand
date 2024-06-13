<?php

namespace App\Entity;

use Symfony\Component\Security\Core\User\UserInterface;
use App\Repository\UserRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
#[UniqueEntity(fields: 'email', message: 'Email doit être unique')]

#[ORM\Entity(repositoryClass: UserRepository::class)]
class User implements UserInterface

{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private $id_user;
    
    #[ORM\Column(name: "nom", type: "string", length: 255)]
    #[Assert\NotBlank(message: 'Le nom est requis')]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Le Nom ne doit pas contenir de chiffres.'
    )]
    private $nom;

    #[ORM\Column(name: "prenom", type: "string", length: 255)]
    #[Assert\NotBlank(message: 'Le prénom est requis')]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Le Prénom ne doit pas contenir de chiffres.'
    )]  
    private $prenom;

    #[ORM\Column(name: "email", type: "string", length: 255)]
    #[Assert\NotBlank(message: 'L\'email est requis')]
    #[Assert\Email(message: 'Veuillez entrer un email valide')]
    private $email;

    #[ORM\Column(name: "password", type: "string", length: 255)]
    #[Assert\NotBlank(message: 'Le mot de passe est requis')]
    #[Assert\Regex(
        pattern: '/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/',
        message: 'Le mot de passe doit contenir au moins une lettre et un chiffre, et avoir au moins 6 caractères.'
    )]
    #[Assert\Length(min: 6, minMessage: 'Le mot de passe doit comporter au moins {{ limit }} caractères')]
    private $password;

    #[ORM\Column(name: "role", type: "string", length: 255)]
    #[Assert\NotBlank(message: 'Le Role  est requis')]
    private $role;

    #[ORM\Column(name: "confirmer_password", type: "string", length: 255, nullable: true)]
    #[Assert\NotBlank(message: 'Le mot de passe de confirmation est requis')]
    #[Assert\EqualTo(propertyPath: 'password', message: 'Les mots de passe ne correspondent pas')]
    private $confirmerPassword;
    #[ORM\Column(name: "image", type: "string", length: 255, nullable: true)]
    private $image;
    #[ORM\Column(name: "bonus", type: "integer", nullable: true)]
private $bonus;
    public function getBonus(): ?int
    {
        return $this->bonus;
    }

    public function setBonus(string $bonus): self
    {
        $this->bonus = $bonus;
        return $this;
    }



    public function getId(): ?int
    {
        return $this->id_user;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): self
    {
        $this->prenom = $prenom;
        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): self
    {
        $this->email = $email;
        return $this;
    }

    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function setPassword(string $password): self
    {
        $this->password = $password;
        return $this;
    }

    public function getRole(): ?string
    {
        return $this->role;
    }

    public function setRole(string $role): self
    {
        $this->role = $role;
        return $this;
    }

    public function getConfirmerPassword(): ?string
    {
        return $this->confirmerPassword;
    }

    public function setConfirmerPassword(?string $confirmerPassword): self
    {
        $this->confirmerPassword = $confirmerPassword;
        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): self
    {
        $this->image = $image;
        return $this;
    }

    public function getUsername(): ?string
    {
        return $this->email;
    }

    public function getRoles(): array
    {
        // Vous pouvez personnaliser cette méthode pour retourner les rôles de l'utilisateur
        return [$this->role];
    }

    public function getSalt()
    {
        // Ajoutez un commentaire d'annotation @return explicite
        return null; // ou tout ce qui est approprié pour votre application
    }

    public function eraseCredentials()
    {
        // Implementer si vous avez besoin d'effacer des données sensibles de l'utilisateur
        // Dans ce cas, il n'y a rien à faire car nous ne stockons pas de données sensibles
    }
    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    public function __toString(): string
    {
        return $this->getNom(); 
    }
}
