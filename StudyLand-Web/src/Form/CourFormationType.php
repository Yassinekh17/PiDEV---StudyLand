<?php

namespace App\Form;

use App\Entity\CourFormation;
use App\Entity\Formation;


use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;

class CourFormationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomCours')
            ->add('descriptionCours', FileType::class, [
                'label' => 'Upload PDF file',
                'required' => true,
                'mapped' => false, // since you may not have a property in your entity for file upload
                'constraints' => [
                    new File([
                        'maxSize' => '4000k', // adjust as needed
                        'mimeTypes' => [
                            'application/pdf',
                            'application/x-pdf',
                        ],
                        'mimeTypesMessage' => 'Please upload a valid PDF document',
                    ])
                ],
            ])
            ->add('idformation', EntityType::class, [
                'class' => Formation::class,
                'choice_label' => 'titre',
                'placeholder' => 'Select a formation', // Optional placeholder
                'mapped' => false, // To prevent Symfony from trying to map this to a property in CourFormation
            ])
;        
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => CourFormation::class,
        ]);
    }
}
