<?php

namespace App\Repository;

use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;


class UserRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, User::class);
    }
    

    public function searchByName($searchTerm)
    {
        return $this->createQueryBuilder('u')
            ->where('LOWER(u.nom) LIKE :searchTerm')
            ->setParameter('searchTerm', '%'.strtolower($searchTerm).'%')
            ->getQuery()
            ->getResult();
    }

    public function countUsersByRole(): array
    {
        return $this->createQueryBuilder('u')
            ->select('u.role, COUNT(u.id_user) as userCount')
            ->groupBy('u.role')
            ->getQuery()
            ->getResult();
    }
            

/**
     * Find a user by email address.
     *
     * @param string $email The email address of the user
     * @return User|null
     */
    public function findByEmail(string $email): ?User
    {
        return $this->findOneBy(['email' => $email]);
    }

    /**
     * Find a user by username.
     *
     * @param string $username The username of the user
     * @return User|null
     */
    public function findByUsername(string $username): ?User
    {
        return $this->findOneBy(['username' => $username]);
    }

    /**
     * Find users by role.
     *
     * @param string $role The role of the users
     * @return User[]
     */
    public function findByRole(string $role): array
    {
        return $this->findBy(['role' => $role]);
    }


}

