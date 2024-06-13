<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
class AddUserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom')
            ->add('prenom')
            ->add('email')
            ->add('image',FileType::class,[
                'label' => 'image',
                'mapped' => false,
                'required' => false,
                
            ])          
             ->add('role', ChoiceType::class, [
                'choices' => [
                    'Admin' => 'Admin',
                    'Formateur' => 'Formateur',
                ],
                'expanded' => true,
                'multiple' => false,
                'choice_attr' => function($choiceValue, $key, $value) {
                    return ['class' => 'inputck'];
                },
            ])
           
            ->add('saveButton', SubmitType::class, [
                'label' => 'Save',
                'attr' => ['class' => 'button1'],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}
