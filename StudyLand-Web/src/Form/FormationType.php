<?php

namespace App\Form;

use App\Entity\Formation;
use App\Entity\Categorie; // Import the Categorie entity
use App\Entity\User;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityRepository;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType; // Import the EntityType
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

class FormationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('titre')
            ->add('description')
            ->add('duree')
            ->add('datedebut')
            ->add('datefin')
            ->add('prix')
            ->add('niveau', ChoiceType::class, [ // Use ChoiceType for the "niveau" field
                'choices' => [ // Define the choices
                    'Bac' => 'Bac',
                    'Master' => 'Master',
                    'Faculté' => 'Faculté',
                ],
                'placeholder' => 'Choose a niveau', // Optional placeholder
                'label' => 'Niveau', // Label for the field
            ])
            ->add('id_user', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'nom',
                'label' => 'Formateur',
                'query_builder' => function (EntityRepository $repository) { // Change type hint to EntityRepository
                    return $repository->createQueryBuilder('u')
                        ->where('u.role = :role')
                        ->setParameter('role', 'Formateur');
                },
            ])
            
            ->add('nomCategorie', EntityType::class, [
                'class' => Categorie::class,
                'choice_label' => 'nomCategorie',
                'placeholder' => 'Select a category', // Optional placeholder
            ])
    
            ->add("save", SubmitType::class);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Formation::class,
        ]);
    }
}
