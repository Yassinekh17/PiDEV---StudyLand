<?php


namespace App\Entity;

use App\Repository\CategorieRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Context\ExecutionContextInterface;


#[ORM\Entity(repositoryClass: CategorieRepository::class)]
class Categorie
{   
    #[ORM\Column(name: "idCategorie", type: "integer")]
    private $idCategorie;

    #[ORM\Id]
    #[ORM\Column(name: "nomCategorie", type: "string", length: 50, nullable: false)]
    #[Assert\NotBlank(message: "Le champ ne doit pas être vide.")]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Le Categorie ne doit pas contenir de chiffres.'
    )]
    private $nomCategorie;

    public function getIdCategorie(): ?int
    {
        return $this->idCategorie;
    }

    public function setIdCategorie(int $idCategorie): self
    {
        $this->idCategorie = $idCategorie;

        return $this;
    }

    public function getNomCategorie(): ?string
    {
        return $this->nomCategorie;
    }

    public function setNomCategorie(string $nomCategorie): self
    {
        $this->nomCategorie = $nomCategorie;

        return $this;
    }
}
