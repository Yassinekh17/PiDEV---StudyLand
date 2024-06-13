<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Context\ExecutionContextInterface;
#[ORM\Entity]
class CourFormation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "IdCour", type: "integer", nullable: false)]
    private $idcour;

    #[ORM\Column(name: "Nom_Cours", type: "string", length: 50, nullable: false)]
    #[Assert\NotBlank(message: "Le champ ne doit pas être vide.")]
    private $nomCours;

    #[ORM\Column(name: "Description_Cours", type: "blob", length: 0, nullable: false)]

    private $descriptionCours;

    #[ORM\ManyToOne(targetEntity: Formation::class)]
    #[ORM\JoinColumn(name: "idFormation", referencedColumnName: "idFormation")]
    private $idformation;

    public function getIdcour(): ?int
    {
        return $this->idcour;
    }

    public function getNomCours(): ?string
    {
        return $this->nomCours;
    }

    public function setNomCours(string $nomCours): static
    {
        $this->nomCours = $nomCours;

        return $this;
    }

    public function getDescriptionCours()
    {
        return $this->descriptionCours;
    }

    public function setDescriptionCours($descriptionCours): static
    {
        $this->descriptionCours = $descriptionCours;

        return $this;
    }

    public function getIdFormation(): ?Formation
    {
        return $this->idformation;
    }

    public function setIdFormation(?Formation $idFormation): self
    {
        $this->idformation = $idFormation;

        return $this;
    }
    public function get(): ?Formation
    {
        return $this->idformation;
    }

}
