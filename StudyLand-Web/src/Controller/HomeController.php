<?php

namespace App\Controller;

use App\Entity\Categorie;
use App\Entity\Formation;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Response;
use App\Form\ProjetType;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\Projet;
use App\Repository\ProjetRepository;
use Doctrine\Persistence\ManagerRegistry;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Writer\Xlsx;
use PhpOffice\PhpSpreadsheet\IOFactory;
use Symfony\Component\HttpFoundation\JsonResponse;
use Endroid\QrCode\Color\Color;
use Endroid\QrCode\QrCode;
use Endroid\QrCode\Writer\PngWriter;
use Endroid\QrCode\Writer\PngDataUriWriter;
use Endroid\QrCode\Writer\Result\ResultInterface;
use Endroid\QrCode\Builder\Builder;
use Endroid\QrCode\Encoding\Encoding;
use Endroid\QrCode\ErrorCorrectionLevel;
use Endroid\QrCode\Label\Font\NotoSans;
use Endroid\QrCode\Label\LabelAlignment;
use Endroid\QrCode\RoundBlockSizeMode;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use Symfony\Component\HttpFoundation\Session\Session;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;

class HomeController extends AbstractController
{
    #[Route('/', name: "home")]
    public function index(): Response
    {
// Fetch formations from the database
$formations = $this->getDoctrine()->getRepository(Formation::class)->findAll();

// Fetch categories from the database
$categories = $this->getDoctrine()->getRepository(Categorie::class)->findAll();

// Pass formations and categories to the template
return $this->render('base.html.twig', [
'Formation' => $formations,
'Categorie' => $categories, // Pass the categories variable to the template
]);

    }
   
    #[Route('/dash', name: "dash")]
    public function indexAdmin(): Response
    {
        return $this->render('admin/admin.html.twig');
    }  

}