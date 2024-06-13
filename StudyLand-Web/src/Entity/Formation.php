<?php

namespace App\Entity;

use App\Repository\FormationRepository;
use Doctrine\ORM\Mapping as ORM;
use App\Entity\User;
use App\Entity\Categorie;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Context\ExecutionContextInterface;

#[ORM\Entity(repositoryClass: FormationRepository::class)]
#[Assert\Callback(callback: 'validateDate')]

class Formation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "idFormation", type: "integer", nullable: false)]
    private $idFormation;

    #[ORM\Column(name: "titre", type: "string", length: 20, nullable: false)]
    #[Assert\NotBlank]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Le Nom de la Formation ne doit pas contenir de chiffres.'
    )]
    private $titre;

    #[ORM\Column(name: "description", type: "string", length: 20, nullable: false)]
    #[Assert\NotBlank]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'La description ne doit pas contenir de chiffres.'
    )]
    private $description;

    #[ORM\Column(name: "duree", type: "integer", nullable: false)]
    #[Assert\Range(
        min: 1,
        minMessage: "The duration must be at least {{ limit }} day"
    )]
    private $duree;

    #[ORM\Column(name: "dateDebut", type: "date", nullable: false)]

    private $datedebut;

    #[ORM\Column(name: "dateFin", type: "date", nullable: false)]

    private $datefin;

    #[ORM\Column(name: "prix", type: "float", precision: 10, scale: 0, nullable: false)]
    #[Assert\Range(
        min: 0,
        minMessage: "The price must be at least {{ limit }} dt"
    )]
    private $prix;

    #[ORM\Column(name: "niveau", type: "string", length: 20, nullable: false)]
    #[Assert\NotBlank]
    private $niveau;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "id_user", referencedColumnName: "id_user")]
    private $id_user;

    #[ORM\ManyToOne(targetEntity: Categorie::class)]
    #[ORM\JoinColumn(name: "nomCategorie", referencedColumnName: "nomCategorie")]
    private $nomCategorie;

    public function getIdformation(): ?int
    {
        return $this->idFormation;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): self
    {
        $this->titre = $titre;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;

        return $this;
    }

    public function getDuree(): ?int
    {
        return $this->duree;
    }

    public function setDuree(int $duree): self
    {
        $this->duree = $duree;

        return $this;
    }

    public function getDatedebut(): ?\DateTimeInterface
    {
        return $this->datedebut;
    }

    public function setDatedebut(\DateTimeInterface $datedebut): self
    {
        $this->datedebut = $datedebut;

        return $this;
    }

    public function getDatefin(): ?\DateTimeInterface
    {
        return $this->datefin;
    }

    public function setDatefin(\DateTimeInterface $datefin): self
    {
        $this->datefin = $datefin;

        return $this;
    }

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(float $prix): self
    {
        $this->prix = $prix;

        return $this;
    }

    public function getNiveau(): ?string
    {
        return $this->niveau;
    }

    public function setNiveau(string $niveau): self
    {
        $this->niveau = $niveau;

        return $this;
    }

    public function getIdUser(): ?User
    {
        return $this->id_user;
    }

    public function setIdUser(?User $id_user): self
    {
        $this->id_user = $id_user;

        return $this;
    }

    public function getNomCategorie(): ?Categorie
    {
        return $this->nomCategorie;
    }

    public function setNomCategorie(?Categorie $nomCategorie): self
    {
        $this->nomCategorie = $nomCategorie;

        return $this;
    }

    public function getUser(): ?string
    {
        return $this->id_user ? $this->id_user->getNom() : null;
    }

    public function setUser(?User $user): self
    {
        $this->id_user = $user;

        return $this;
    }

    public function getCategorie(): ?String
    {
        return $this->nomCategorie ? $this->nomCategorie->getNomCategorie() : null;
    }

    public function setCategorie(?Categorie $categorie): self
    {
        $this->nomCategorie = $categorie;

        return $this;
    }

    public function validateDate(ExecutionContextInterface $context)
    {
        $today = new \DateTime('today');

        // Check if datedebut is not null and is before today
        if ($this->datedebut && $this->datedebut < $today) {
            $context->buildViolation('The start date must be today or later')
                ->atPath('datedebut')
                ->addViolation();
        }

        // Check if datefin is not null and is before today
        if ($this->datefin && $this->datefin < $today) {
            $context->buildViolation('The end date must be today or later')
                ->atPath('datefin')
                ->addViolation();
        }

        // Check if datefin is after datedebut
        if ($this->datedebut && $this->datefin && $this->datefin <= $this->datedebut) {
            $context->buildViolation('The end date must be after the start date.')
                ->atPath('datefin')
                ->addViolation();
        }
    }
    public function getCategories(): ?Categorie
{
    return $this->nomCategorie;
}
}
