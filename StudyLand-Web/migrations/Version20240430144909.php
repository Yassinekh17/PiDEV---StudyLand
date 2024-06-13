<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240430144909 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE reset_password_request (id INT AUTO_INCREMENT NOT NULL, id_user INT NOT NULL, selector VARCHAR(20) NOT NULL, hashed_token VARCHAR(100) NOT NULL, requested_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', expires_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', INDEX IDX_7CE748A6B3CA4B (id_user), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL, available_at DATETIME NOT NULL, delivered_at DATETIME DEFAULT NULL, INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE reset_password_request ADD CONSTRAINT FK_7CE748A6B3CA4B FOREIGN KEY (id_user) REFERENCES user (id_user)');
        $this->addSql('ALTER TABLE achat DROP FOREIGN KEY achat_ibfk_1');
        $this->addSql('ALTER TABLE achat DROP FOREIGN KEY achat_ibfk_2');
        $this->addSql('ALTER TABLE discussion DROP FOREIGN KEY discussion_ibfk_2');
        $this->addSql('ALTER TABLE discussion DROP FOREIGN KEY discussion_ibfk_1');
        $this->addSql('ALTER TABLE evaluationquestion DROP FOREIGN KEY evaluationquestion_ibfk_1');
        $this->addSql('ALTER TABLE evaluationquestion DROP FOREIGN KEY evaluationquestion_ibfk_2');
        $this->addSql('ALTER TABLE favoris DROP FOREIGN KEY fk');
        $this->addSql('ALTER TABLE favoris DROP FOREIGN KEY pk');
        $this->addSql('ALTER TABLE formation DROP FOREIGN KEY formation_ibfk_1');
        $this->addSql('ALTER TABLE formation DROP FOREIGN KEY fk_user_id');
        $this->addSql('ALTER TABLE inscrit DROP FOREIGN KEY inscrit_ibfk_1');
        $this->addSql('ALTER TABLE inscrit DROP FOREIGN KEY inscrit_ibfk_2');
        $this->addSql('ALTER TABLE message DROP FOREIGN KEY fk_user_id_message');
        $this->addSql('ALTER TABLE panier DROP FOREIGN KEY panier_ibfk_1');
        $this->addSql('ALTER TABLE panier DROP FOREIGN KEY panier_ibfk_2');
        $this->addSql('ALTER TABLE reponse DROP FOREIGN KEY reponse_ibfk_1');
        $this->addSql('ALTER TABLE taches_projet DROP FOREIGN KEY Foreign key');
        $this->addSql('DROP TABLE achat');
        $this->addSql('DROP TABLE categorie');
        $this->addSql('DROP TABLE cour_formation');
        $this->addSql('DROP TABLE discussion');
        $this->addSql('DROP TABLE evaluation');
        $this->addSql('DROP TABLE evaluationquestion');
        $this->addSql('DROP TABLE favoris');
        $this->addSql('DROP TABLE formation');
        $this->addSql('DROP TABLE inscrit');
        $this->addSql('DROP TABLE message');
        $this->addSql('DROP TABLE panier');
        $this->addSql('DROP TABLE projet');
        $this->addSql('DROP TABLE question');
        $this->addSql('DROP TABLE reponse');
        $this->addSql('DROP TABLE taches_projet');
        $this->addSql('ALTER TABLE user CHANGE image image VARCHAR(255) DEFAULT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE achat (id_user INT NOT NULL, idAchat INT AUTO_INCREMENT NOT NULL, idFormation INT NOT NULL, dateAjout DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, cardNumber VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, exprMonth VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, exprYear VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, cvv VARCHAR(3) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, INDEX idFormation (idFormation), INDEX id_user (id_user), PRIMARY KEY(idAchat)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE categorie (idCategorie INT AUTO_INCREMENT NOT NULL, nomCategorie VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, INDEX nomCategorie (nomCategorie), PRIMARY KEY(idCategorie)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE cour_formation (IdCour INT AUTO_INCREMENT NOT NULL, Nom_Cours VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, Description_Cours LONGBLOB NOT NULL, idFormation INT DEFAULT NULL, INDEX idFormation (idFormation), PRIMARY KEY(IdCour)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE discussion (id_discussion INT AUTO_INCREMENT NOT NULL, id_user1 INT DEFAULT NULL, id_user2 INT DEFAULT NULL, INDEX discussion_ibfk_1 (id_user1), INDEX discussion_ibfk_2 (id_user2), PRIMARY KEY(id_discussion)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE evaluation (id_evaluation INT AUTO_INCREMENT NOT NULL, titre_evaluation VARCHAR(30) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, description VARCHAR(70) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, Difficulte VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, nb_questions INT NOT NULL, Duree TIME NOT NULL, Resultat DOUBLE PRECISION NOT NULL, testDate DATE DEFAULT NULL, Createur VARCHAR(30) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, prix DOUBLE PRECISION NOT NULL, domaine VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, PRIMARY KEY(id_evaluation)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE evaluationquestion (id_evaluationquestion INT AUTO_INCREMENT NOT NULL, id_question INT NOT NULL, id_evaluation INT NOT NULL, INDEX id_evaluation (id_evaluation), INDEX id_question_2 (id_question), INDEX id_question (id_question, id_evaluation), PRIMARY KEY(id_evaluationquestion)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE favoris (id_user INT NOT NULL, idFavoris INT AUTO_INCREMENT NOT NULL, idFormation INT NOT NULL, dateAjout DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, type VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, INDEX pk (id_user), INDEX fk (idFormation), PRIMARY KEY(idFavoris)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE formation (id_user INT DEFAULT NULL, idFormation INT AUTO_INCREMENT NOT NULL, titre VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, description VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, duree INT NOT NULL, dateDebut DATE NOT NULL, dateFin DATE NOT NULL, prix DOUBLE PRECISION NOT NULL, niveau VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, nomCategorie VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, INDEX nomCategorie (nomCategorie), INDEX idFormation (idFormation), INDEX fk_user_id (id_user), PRIMARY KEY(idFormation)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE inscrit (idInscrit INT AUTO_INCREMENT NOT NULL, id_user INT NOT NULL, idFormation INT NOT NULL, dateAjout DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, type VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, INDEX id_user (id_user), INDEX idFormation (idFormation), PRIMARY KEY(idInscrit)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE message (id_message INT AUTO_INCREMENT NOT NULL, id_sender INT DEFAULT NULL, message VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, date DATE DEFAULT NULL, id_diss INT DEFAULT NULL, INDEX fk_user_id_message (id_sender), PRIMARY KEY(id_message)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE panier (id_panier INT AUTO_INCREMENT NOT NULL, id_user INT NOT NULL, id_formation INT NOT NULL, date_ajout DATE NOT NULL, INDEX id_user (id_user, id_formation), INDEX id_formation (id_formation), INDEX IDX_24CC0DF26B3CA4B (id_user), PRIMARY KEY(id_panier)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE projet (id_projet INT AUTO_INCREMENT NOT NULL, NomProjet VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, DescProjet VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, Date_D DATE NOT NULL, Date_F DATE NOT NULL, nb_taches INT NOT NULL, PRIMARY KEY(id_projet)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE question (idQuestion INT AUTO_INCREMENT NOT NULL, enonce VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, domaine VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, PRIMARY KEY(idQuestion)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE reponse (idReponse INT AUTO_INCREMENT NOT NULL, contenu VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, idQuestion INT NOT NULL, status VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, INDEX idQuestion (idQuestion), PRIMARY KEY(idReponse)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE taches_projet (id_taches INT AUTO_INCREMENT NOT NULL, id_projet INT NOT NULL, nom_tache VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, Desc_tache VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, Date_D DATE NOT NULL, Date_F DATE NOT NULL, statut VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, Responsable VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, INDEX Foreign key (id_projet), PRIMARY KEY(id_taches)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('ALTER TABLE achat ADD CONSTRAINT achat_ibfk_1 FOREIGN KEY (idFormation) REFERENCES formation (idFormation) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE achat ADD CONSTRAINT achat_ibfk_2 FOREIGN KEY (id_user) REFERENCES user (id_user) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE discussion ADD CONSTRAINT discussion_ibfk_2 FOREIGN KEY (id_user2) REFERENCES user (id_user)');
        $this->addSql('ALTER TABLE discussion ADD CONSTRAINT discussion_ibfk_1 FOREIGN KEY (id_user1) REFERENCES user (id_user)');
        $this->addSql('ALTER TABLE evaluationquestion ADD CONSTRAINT evaluationquestion_ibfk_1 FOREIGN KEY (id_question) REFERENCES question (idQuestion) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE evaluationquestion ADD CONSTRAINT evaluationquestion_ibfk_2 FOREIGN KEY (id_evaluation) REFERENCES evaluation (id_evaluation) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE favoris ADD CONSTRAINT fk FOREIGN KEY (idFormation) REFERENCES formation (idFormation) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE favoris ADD CONSTRAINT pk FOREIGN KEY (id_user) REFERENCES user (id_user) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE formation ADD CONSTRAINT formation_ibfk_1 FOREIGN KEY (nomCategorie) REFERENCES categorie (nomCategorie)');
        $this->addSql('ALTER TABLE formation ADD CONSTRAINT fk_user_id FOREIGN KEY (id_user) REFERENCES user (id_user)');
        $this->addSql('ALTER TABLE inscrit ADD CONSTRAINT inscrit_ibfk_1 FOREIGN KEY (idFormation) REFERENCES formation (idFormation) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE inscrit ADD CONSTRAINT inscrit_ibfk_2 FOREIGN KEY (idInscrit) REFERENCES user (id_user) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE message ADD CONSTRAINT fk_user_id_message FOREIGN KEY (id_sender) REFERENCES user (id_user) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE panier ADD CONSTRAINT panier_ibfk_1 FOREIGN KEY (id_user) REFERENCES user (id_user) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE panier ADD CONSTRAINT panier_ibfk_2 FOREIGN KEY (id_formation) REFERENCES formation (idFormation) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE reponse ADD CONSTRAINT reponse_ibfk_1 FOREIGN KEY (idQuestion) REFERENCES question (idQuestion) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE taches_projet ADD CONSTRAINT Foreign key FOREIGN KEY (id_projet) REFERENCES projet (id_projet) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE reset_password_request DROP FOREIGN KEY FK_7CE748A6B3CA4B');
        $this->addSql('DROP TABLE reset_password_request');
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('ALTER TABLE user CHANGE image image VARCHAR(255) DEFAULT \'C:\\\\pidev\\\\StudyLand\\\\src\\\\main\\\\resources\\\\src\\\\77.png\'');
    }
}
