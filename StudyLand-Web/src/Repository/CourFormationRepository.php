<?php

namespace App\Repository;

use App\Entity\CourFormation;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;


class CourFormationRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, CourFormation::class);
    }
    public function findByFormationPrice(float $price): array
    {
        $query = $this->createQueryBuilder('cf')
            ->leftJoin('cf.idformation', 'f')
            ->andWhere('f.prix = :price')
            ->setParameter('price', $price)
            ->getQuery();
        
        return $query->getResult();
        
    }
}