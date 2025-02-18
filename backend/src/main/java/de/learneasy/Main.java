package de.learneasy;

import de.learneasy.model.*;
import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyOfChapterExerciseTemplate;
import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyWord;
import de.learneasy.repositories.BooksRepository;
import de.learneasy.repositories.ExerciseTemplatesRepository;
import de.learneasy.repositories.SchoolsRepository;
import de.learneasy.repositories.VocabularyWordsRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String@NotNull [] args) {
        final @NotNull ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(LearnEasy.class, args);
        printExampleSchool(configurableApplicationContext.getBean(SchoolsRepository.class));
        createFrenchBookIfNotExists(configurableApplicationContext.getBean(BooksRepository.class), configurableApplicationContext.getBean(ExerciseTemplatesRepository.class), configurableApplicationContext.getBean(VocabularyWordsRepository.class));
    }

    public static void printExampleSchool(SchoolsRepository schoolsRepository) {
        final @NotNull School exampleSchool = schoolsRepository.getExampleSchool();
        System.out.println();
        System.out.println("EXAMPLE SCHOOL:");
        System.out.println("ID: " + exampleSchool.getId());
        System.out.println("Name: " + exampleSchool.getName());
        System.out.println("E-Mail: " + exampleSchool.getEmail());
        System.out.println("Registration Code: " + exampleSchool.getRegistrationCode().getCode());
        System.out.println("Admin Table:");
        final @NotNull Map<@NotNull Integer, @NotNull Character> adminTable = exampleSchool.getAdminTable();
        for (int key : adminTable.keySet()) System.out.print(key + "\t");
        System.out.println();
        for (Integer key : adminTable.keySet()) System.out.print(exampleSchool.getAdminTable().get(key) + "\t");
        System.out.println();
        System.out.println();
    }

    public static void createFrenchBookIfNotExists(@NotNull BooksRepository booksRepository, @NotNull ExerciseTemplatesRepository exerciseTemplatesRepository, @NotNull VocabularyWordsRepository vocabularyWordsRepository) {
        final @NotNull String frenchbookIsbn = "978-3061214302";
        final @Nullable Book existingBook = booksRepository.findByIsbn(frenchbookIsbn).orElse(null);

        if(existingBook == null)
        {
            final @NotNull String firstChapter = "Unité 1 La rentrée";
            final @NotNull String secondChapter = "Unité 2 À la maison";

            final @NotNull List<@NotNull VocabularyWord> vocabulary = new ArrayList<>();
            //Unité 1 Volet 1
            vocabulary.add(new VocabularyWord("la rentrée", "der Schuljahresbeginn (in Frankreich: Anfang September)", firstChapter, 1, getVocabularyFile(firstChapter+"-1.png"), getVocabularyFile(firstChapter+"-1.mp3")));
            vocabulary.add(new VocabularyWord("Salut! (fam.)", "Servus!/Hallo!/Tschüss!", firstChapter, 2, getVocabularyFile(firstChapter+"-2.png"), getVocabularyFile(firstChapter+"-2.mp3")));
            vocabulary.add(new VocabularyWord("Ça va?", "Wie geht's?/Geht's dir gut?", firstChapter, 3, getVocabularyFile(firstChapter+"-3.png"), getVocabularyFile(firstChapter+"-3.mp3")));
            vocabulary.add(new VocabularyWord("Ça va.", "Es geht (mir) gut.", firstChapter, 4, getVocabularyFile(firstChapter+"-4.png"), getVocabularyFile(firstChapter+"-4.mp3")));
            vocabulary.add(new VocabularyWord("Et toi?", "Und dir?/Und du?", firstChapter, 5, getVocabularyFile(firstChapter+"-5.png"), getVocabularyFile(firstChapter+"-5.mp3")));
            vocabulary.add(new VocabularyWord("et", "und", firstChapter, 6, getVocabularyFile(firstChapter+"-6.png"), getVocabularyFile(firstChapter+"-6.mp3")));
            vocabulary.add(new VocabularyWord("toi", "du (betonte Form des Personalpronomens)", firstChapter, 7, getVocabularyFile(firstChapter+"-7.png"), getVocabularyFile(firstChapter+"-7.mp3")));
            vocabulary.add(new VocabularyWord("bof (fam.)", "Na ja./Es geht so.", firstChapter, 8, getVocabularyFile(firstChapter+"-8.png"), getVocabularyFile(firstChapter+"-8.mp3")));
            vocabulary.add(new VocabularyWord("À plus! (fam.)", "Bis später!", firstChapter, 9, getVocabularyFile(firstChapter+"-9.png"), getVocabularyFile(firstChapter+"-9.mp3")));
            vocabulary.add(new VocabularyWord("À demain!", "Bis morgen!", firstChapter, 10, getVocabularyFile(firstChapter+"-10.png"), getVocabularyFile(firstChapter+"-10.mp3")));
            vocabulary.add(new VocabularyWord("Bonjour", "Guten Tag!, Guten Morgen!, Grüß Gott!", firstChapter, 11, getVocabularyFile(firstChapter+"-11.png"), getVocabularyFile(firstChapter+"-11.mp3")));
            vocabulary.add(new VocabularyWord("Madame/Mme", "Frau (Anrede)", firstChapter, 12, getVocabularyFile(firstChapter+"-12.png"), getVocabularyFile(firstChapter+"-12.mp3")));
            vocabulary.add(new VocabularyWord("Au revoir!", "Auf Wiedersehen!", firstChapter, 13, getVocabularyFile(firstChapter+"-11.png"), getVocabularyFile(firstChapter+"-13.mp3")));
            vocabulary.add(new VocabularyWord("super", "super, toll", firstChapter, 14, getVocabularyFile(firstChapter+"-14.png"), getVocabularyFile(firstChapter+"-14.mp3")));
            vocabulary.add(new VocabularyWord("Monsieur/M.", "Herr (Anrede)", firstChapter, 15, getVocabularyFile(firstChapter+"-15.png"), getVocabularyFile(firstChapter+"-15.mp3")));

            //Unité 1 Volet 2
            vocabulary.add(new VocabularyWord("c'est", "das ist", firstChapter, 16, getVocabularyFile(firstChapter+"-16.png"), getVocabularyFile(firstChapter+"-16.mp3")));
            vocabulary.add(new VocabularyWord("être", "sein", firstChapter, 17, getVocabularyFile(firstChapter+"-17.png"), getVocabularyFile(firstChapter+"-17.mp3")));
            vocabulary.add(new VocabularyWord("voilà", "das ist, Da kommt...", firstChapter, 18, getVocabularyFile(firstChapter+"-18.png"), getVocabularyFile(firstChapter+"-18.mp3")));
            vocabulary.add(new VocabularyWord("la classe de sixième A", "die Sechs A (6A)", firstChapter, 19, getVocabularyFile(firstChapter+"-19.png"), getVocabularyFile(firstChapter+"-19.mp3")));
            vocabulary.add(new VocabularyWord("la classe", "die Klasse", firstChapter, 20, getVocabularyFile(firstChapter+"-20.png"), getVocabularyFile(firstChapter+"-20.mp3")));
            vocabulary.add(new VocabularyWord("la sixième/la 6ᵉ", "Erste Jahrgangsstufe nach der fünfjährigen Grundschule", firstChapter, 21, getVocabularyFile(firstChapter+"-21.png"), getVocabularyFile(firstChapter+"-21.mp3")));
            vocabulary.add(new VocabularyWord("je suis", "ich bin (1. Pers. Sg. von être)", firstChapter, 22, getVocabularyFile(firstChapter+"-22.png"), getVocabularyFile(firstChapter+"-22.mp3")));
            vocabulary.add(new VocabularyWord("le/la professeur de français", "der/die Französischlehrer/in", firstChapter, 23, getVocabularyFile(firstChapter+"-23.png"), getVocabularyFile(firstChapter+"-23.mp3")));
            vocabulary.add(new VocabularyWord("le/la professeur", "der/die Lehrer/in", firstChapter, 24, getVocabularyFile(firstChapter+"-24.png"), getVocabularyFile(firstChapter+"-24.mp3")));
            vocabulary.add(new VocabularyWord("le français", "Französisch (Unterrichtsfach), die französische Sprache", firstChapter, 25, getVocabularyFile(firstChapter+"-25.png"), getVocabularyFile(firstChapter+"-25.mp3")));
            vocabulary.add(new VocabularyWord("oui", "ja", firstChapter, 26, getVocabularyFile(firstChapter+"-26.png"), getVocabularyFile(firstChapter+"-26.mp3")));
            vocabulary.add(new VocabularyWord("je m'appelle", "ich heiße (1. Pers. Sg. von s'appeler)", firstChapter, 27, getVocabularyFile(firstChapter+"-27.png"), getVocabularyFile(firstChapter+"-27.mp3")));
            vocabulary.add(new VocabularyWord("ah", "ach, ach so", firstChapter, 28, getVocabularyFile(firstChapter+"-28.png"), getVocabularyFile(firstChapter+"-28.mp3")));
            vocabulary.add(new VocabularyWord("pardon", "Verzeihung", firstChapter, 29, getVocabularyFile(firstChapter+"-29.png"), getVocabularyFile(firstChapter+"-29.mp3")));
            vocabulary.add(new VocabularyWord("C'est moi.", "Ich bin's./Das bin ich.", firstChapter, 30, getVocabularyFile(firstChapter+"-30.png"), getVocabularyFile(firstChapter+"-30.mp3")));
            vocabulary.add(new VocabularyWord("moi", "ich (betonte Form des Personalpronomens)", firstChapter, 31, getVocabularyFile(firstChapter+"-31.png"), getVocabularyFile(firstChapter+"-31.mp3")));
            vocabulary.add(new VocabularyWord("alors", "also", firstChapter, 32, getVocabularyFile(firstChapter+"-32.png"), getVocabularyFile(firstChapter+"-32.mp3")));
            vocabulary.add(new VocabularyWord("tu es", "du bist (2. Pers. Sg. von être)", firstChapter, 33, getVocabularyFile(firstChapter+"-33.png"), getVocabularyFile(firstChapter+"-33.mp3")));
            vocabulary.add(new VocabularyWord("tu t'appelles", "du heißt (2. Pers. Sg.)", firstChapter, 34, getVocabularyFile(firstChapter+"-34.png"), getVocabularyFile(firstChapter+"-34.mp3")));
            vocabulary.add(new VocabularyWord("Avec un C?", "Mit einem C?", firstChapter, 35, getVocabularyFile(firstChapter+"-35.png"), getVocabularyFile(firstChapter+"-35.mp3")));
            vocabulary.add(new VocabularyWord("Non? (fam.)", "Nicht wahr?", firstChapter, 36, getVocabularyFile(firstChapter+"-36.png"), getVocabularyFile(firstChapter+"-36.mp3")));
            vocabulary.add(new VocabularyWord("Tu es d'où?", "Woher kommst du?", firstChapter, 37, getVocabularyFile(firstChapter+"-37.png"), getVocabularyFile(firstChapter+"-37.mp3")));
            vocabulary.add(new VocabularyWord("avec", "mit", firstChapter, 38, getVocabularyFile(firstChapter+"-38.png"), getVocabularyFile(firstChapter+"-38.mp3")));
            vocabulary.add(new VocabularyWord("un", "ein", firstChapter, 39, getVocabularyFile(firstChapter+"-39.png"), getVocabularyFile(firstChapter+"-39.mp3")));
            vocabulary.add(new VocabularyWord("non", "nein", firstChapter, 40, getVocabularyFile(firstChapter+"-40.png"), getVocabularyFile(firstChapter+"-40.mp3")));
            vocabulary.add(new VocabularyWord("il est", "er ist (3. Pers. Sg. m. von être)", firstChapter, 41, getVocabularyFile(firstChapter+"-41.png"), getVocabularyFile(firstChapter+"-41.mp3")));
            vocabulary.add(new VocabularyWord("de", "aus", firstChapter, 42, getVocabularyFile(firstChapter+"-42.png"), getVocabularyFile(firstChapter+"-42.mp3")));
            vocabulary.add(new VocabularyWord("Berlin", "Berlin", firstChapter, 43, getVocabularyFile(firstChapter+"-43.png"), getVocabularyFile(firstChapter+"-43.mp3")));
            vocabulary.add(new VocabularyWord("très", "sehr", firstChapter, 44, getVocabularyFile(firstChapter+"-44.png"), getVocabularyFile(firstChapter+"-44.mp3")));
            vocabulary.add(new VocabularyWord("bien", "gut", firstChapter, 45, getVocabularyFile(firstChapter+"-45.png"), getVocabularyFile(firstChapter+"-45.mp3")));
            vocabulary.add(new VocabularyWord("elle est", "sie ist (3. Pers. Sg. f. von être)", firstChapter, 46, getVocabularyFile(firstChapter+"-46.png"), getVocabularyFile(firstChapter+"-46.mp3")));
            vocabulary.add(new VocabularyWord("là", "da/hier", firstChapter, 47, getVocabularyFile(firstChapter+"-47.png"), getVocabularyFile(firstChapter+"-47.mp3")));
            vocabulary.add(new VocabularyWord("euh", "äh", firstChapter, 48, getVocabularyFile(firstChapter+"-48.png"), getVocabularyFile(firstChapter+"-48.mp3")));
            vocabulary.add(new VocabularyWord("deux", "zwei", firstChapter, 49, getVocabularyFile(firstChapter+"-49.png"), getVocabularyFile(firstChapter+"-49.mp3")));

            //Unité 1 Volet 3
            vocabulary.add(new VocabularyWord("la récréation", "die Pause", firstChapter, 50, getVocabularyFile(firstChapter+"-50.png"), getVocabularyFile(firstChapter+"-50.mp3")));
            vocabulary.add(new VocabularyWord("nous sommes", "wir sind (1. Pers. Pl. von être)", firstChapter, 51, getVocabularyFile(firstChapter+"-51.png"), getVocabularyFile(firstChapter+"-51.mp3")));
            vocabulary.add(new VocabularyWord("à", "in", firstChapter, 52, getVocabularyFile(firstChapter+"-52.png"), getVocabularyFile(firstChapter+"-52.mp3")));
            vocabulary.add(new VocabularyWord("Strasbourg", "Straßburg", firstChapter, 53, getVocabularyFile(firstChapter+"-53.png"), getVocabularyFile(firstChapter+"-53.mp3")));
            vocabulary.add(new VocabularyWord("l'école (f.)", "die Schule", firstChapter, 54, getVocabularyFile(firstChapter+"-54.png"), getVocabularyFile(firstChapter+"-54.mp3")));
            vocabulary.add(new VocabularyWord("Maxime Alexandre", "elsässischer Dichter", firstChapter, 55, getVocabularyFile(firstChapter+"-55.png"), getVocabularyFile(firstChapter+"-55.mp3")));
            vocabulary.add(new VocabularyWord("l'élève (m./f.)", "der/die Schüler/in", firstChapter, 56, getVocabularyFile(firstChapter+"-56.png"), getVocabularyFile(firstChapter+"-56.mp3")));
            vocabulary.add(new VocabularyWord("le surveillant/la surveillante", "die Aufsichtsperson", firstChapter, 57, getVocabularyFile(firstChapter+"-57.png"), getVocabularyFile(firstChapter+"-57.mp3")));
            vocabulary.add(new VocabularyWord("ils sont", "sie sind (3. Pers. Pl. m. von être)", firstChapter, 58, getVocabularyFile(firstChapter+"-58.png"), getVocabularyFile(firstChapter+"-58.mp3")));
            vocabulary.add(new VocabularyWord("dans", "in", firstChapter, 59, getVocabularyFile(firstChapter+"-59.png"), getVocabularyFile(firstChapter+"-59.mp3")));
            vocabulary.add(new VocabularyWord("la cour", "der Schulhof", firstChapter, 60, getVocabularyFile(firstChapter+"-60.png"), getVocabularyFile(firstChapter+"-60.mp3")));
            vocabulary.add(new VocabularyWord("comment", "wie", firstChapter, 61, getVocabularyFile(firstChapter+"-61.png"), getVocabularyFile(firstChapter+"-61.mp3")));
            vocabulary.add(new VocabularyWord("ou", "oder", firstChapter, 62, getVocabularyFile(firstChapter+"-62.png"), getVocabularyFile(firstChapter+"-62.mp3")));
            vocabulary.add(new VocabularyWord("nouveau/nouvelle (m./f.)", "neu", firstChapter, 63, getVocabularyFile(firstChapter+"-63.png"), getVocabularyFile(firstChapter+"-63.mp3")));
            vocabulary.add(new VocabularyWord("le garçon", "der Junge, der Bub", firstChapter, 64, getVocabularyFile(firstChapter+"-64.png"), getVocabularyFile(firstChapter+"-64.mp3")));
            vocabulary.add(new VocabularyWord("C'est qui?", "Wer ist das?", firstChapter, 65, getVocabularyFile(firstChapter+"-65.png"), getVocabularyFile(firstChapter+"-65.mp3")));
            vocabulary.add(new VocabularyWord("Je ne sais pas.", "Ich weiß es nicht.", firstChapter, 66, getVocabularyFile(firstChapter+"-66.png"), getVocabularyFile(firstChapter+"-66.mp3")));
            vocabulary.add(new VocabularyWord("elles sont", "sie sind (Personalpron. 3. Pers. Pl. von être)", firstChapter, 67, getVocabularyFile(firstChapter+"-67.png"), getVocabularyFile(firstChapter+"-67.mp3")));
            vocabulary.add(new VocabularyWord("cool", "cool", firstChapter, 68, getVocabularyFile(firstChapter+"-68.png"), getVocabularyFile(firstChapter+"-68.mp3")));
            vocabulary.add(new VocabularyWord("il/elle s'appelle", "er/sie heißt (3. Pers. Sg. von s'appeler)", firstChapter, 69, getVocabularyFile(firstChapter+"-69.png"), getVocabularyFile(firstChapter+"-69.mp3")));
            vocabulary.add(new VocabularyWord("regarde", "schau mal, guck mal (imperativ 2. Pers. Sg. von regarder)", firstChapter, 70, getVocabularyFile(firstChapter+"-70.png"), getVocabularyFile(firstChapter+"-70.mp3")));
            vocabulary.add(new VocabularyWord("regarder (qn/qc)", "jdn/etw. ansehen", firstChapter, 71, getVocabularyFile(firstChapter+"-71.png"), getVocabularyFile(firstChapter+"-71.mp3")));
            vocabulary.add(new VocabularyWord("en cinquième", "in der siebten Klasse", firstChapter, 72, getVocabularyFile(firstChapter+"-72.png"), getVocabularyFile(firstChapter+"-72.mp3")));
            vocabulary.add(new VocabularyWord("la cinquième/5ᵉ", "entspricht meistens der 7. Klasse in Deutschland", firstChapter, 73, getVocabularyFile(firstChapter+"-73.png"), getVocabularyFile(firstChapter+"-73.mp3")));
            vocabulary.add(new VocabularyWord("de", "von", firstChapter, 74, getVocabularyFile(firstChapter+"-74.png"), getVocabularyFile(firstChapter+"-74.mp3")));
            vocabulary.add(new VocabularyWord("la fille", "das Mädchen", firstChapter, 75, getVocabularyFile(firstChapter+"-75.png"), getVocabularyFile(firstChapter+"-75.mp3")));
            vocabulary.add(new VocabularyWord("aussi", "auch", firstChapter, 76, getVocabularyFile(firstChapter+"-76.png"), getVocabularyFile(firstChapter+"-76.mp3")));
            vocabulary.add(new VocabularyWord("pas mal", "nicht schlecht", firstChapter, 77, getVocabularyFile(firstChapter+"-77.png"), getVocabularyFile(firstChapter+"-77.mp3")));
            vocabulary.add(new VocabularyWord("l'ami/l'amie (m./f.)", "der/die Freund/in", firstChapter, 78, getVocabularyFile(firstChapter+"-78.png"), getVocabularyFile(firstChapter+"-78.mp3")));
            vocabulary.add(new VocabularyWord("on est (fam.)", "wir sind (wörtlich: man ist 3. Pers. Sg. von être)", firstChapter, 79, getVocabularyFile(firstChapter+"-79.png"), getVocabularyFile(firstChapter+"-79.mp3")));
            vocabulary.add(new VocabularyWord("on", "man (Personalpronomen 3. Pers. Sg.)", firstChapter, 80, getVocabularyFile(firstChapter+"-80.png"), getVocabularyFile(firstChapter+"-80.mp3")));
            vocabulary.add(new VocabularyWord("ensemble", "zusammen", firstChapter, 81, getVocabularyFile(firstChapter+"-81.png"), getVocabularyFile(firstChapter+"-81.mp3")));
            vocabulary.add(new VocabularyWord("C'est trop cool!", "Das ist total cool!", firstChapter, 82, getVocabularyFile(firstChapter+"-82.png"), getVocabularyFile(firstChapter+"-82.mp3")));
            vocabulary.add(new VocabularyWord("vous êtes", "ihr seid (2. Pers. Pl. von être, auch: Sie sind wenn du jdn siezt)", firstChapter, 83, getVocabularyFile(firstChapter+"-83.png"), getVocabularyFile(firstChapter+"-83.mp3")));

            //Unité 1 Module 1
            vocabulary.add(new VocabularyWord("le crayon de couleur", "der Buntstift", firstChapter, 84, getVocabularyFile(firstChapter+"-84.png"), getVocabularyFile(firstChapter+"-84.mp3")));
            vocabulary.add(new VocabularyWord("le crayon", "der Bleistift", firstChapter, 85, getVocabularyFile(firstChapter+"-85.png"), getVocabularyFile(firstChapter+"-85.mp3")));
            vocabulary.add(new VocabularyWord("le stylo", "der Kugelschreiber", firstChapter, 86, getVocabularyFile(firstChapter+"-86.png"), getVocabularyFile(firstChapter+"-86.mp3")));
            vocabulary.add(new VocabularyWord("l'effaceur (m.)", "der Tintenkiller", firstChapter, 87, getVocabularyFile(firstChapter+"-87.png"), getVocabularyFile(firstChapter+"-87.mp3")));
            vocabulary.add(new VocabularyWord("la règle", "das Lineal", firstChapter, 88, getVocabularyFile(firstChapter+"-88.png"), getVocabularyFile(firstChapter+"-88.mp3")));
            vocabulary.add(new VocabularyWord("le cahier", "das Heft", firstChapter, 89, getVocabularyFile(firstChapter+"-89.png"), getVocabularyFile(firstChapter+"-89.mp3")));
            vocabulary.add(new VocabularyWord("le papier", "das Papier", firstChapter, 90, getVocabularyFile(firstChapter+"-90.png"), getVocabularyFile(firstChapter+"-90.mp3")));
            vocabulary.add(new VocabularyWord("le classeur", "der Ordner", firstChapter, 91, getVocabularyFile(firstChapter+"-91.png"), getVocabularyFile(firstChapter+"-91.mp3")));
            vocabulary.add(new VocabularyWord("Comment est-ce qu'on dit ...?", "Wie sagt man ...?", firstChapter, 92, getVocabularyFile(firstChapter+"-92.png"), getVocabularyFile(firstChapter+"-92.mp3")));
            vocabulary.add(new VocabularyWord("en français/en allemand", "auf Französisch/auf Deutsch", firstChapter, 93, getVocabularyFile(firstChapter+"-93.png"), getVocabularyFile(firstChapter+"-93.mp3")));
            vocabulary.add(new VocabularyWord("On dit ...", "Man sagt ...", firstChapter, 94, getVocabularyFile(firstChapter+"-94.png"), getVocabularyFile(firstChapter+"-94.mp3")));
            vocabulary.add(new VocabularyWord("Comment est-ce qu'on écrit ...?", "Wie schreibt man ...?", firstChapter, 95, getVocabularyFile(firstChapter+"-95.png"), getVocabularyFile(firstChapter+"-95.mp3")));
            vocabulary.add(new VocabularyWord("On écrit ...", "Man schreibt ...", firstChapter, 96, getVocabularyFile(firstChapter+"-96.png"), getVocabularyFile(firstChapter+"-96.mp3")));

            //Unité 2 Volet 1
            vocabulary.add(new VocabularyWord("à la maison", "zu Hause, daheim", secondChapter, 1, getVocabularyFile(secondChapter+"-1.png"), getVocabularyFile(secondChapter+"-1.mp3")));
            vocabulary.add(new VocabularyWord("la maison", "das Haus", secondChapter, 2, getVocabularyFile(secondChapter+"-2.png"), getVocabularyFile(secondChapter+"-2.mp3")));
            vocabulary.add(new VocabularyWord("chez moi", "bei mir (zu Hause)", secondChapter, 3, getVocabularyFile(secondChapter+"-3.png"), getVocabularyFile(secondChapter+"-3.mp3")));
            vocabulary.add(new VocabularyWord("chez", "bei", secondChapter, 4, getVocabularyFile(secondChapter+"-4.png"), getVocabularyFile(secondChapter+"-4.mp3")));
            vocabulary.add(new VocabularyWord("un/une", "ein/eine", secondChapter, 5, getVocabularyFile(secondChapter+"-5.png"), getVocabularyFile(secondChapter+"-5.mp3")));
            vocabulary.add(new VocabularyWord("l'armoire (f.)", "der Schrank", secondChapter, 6, getVocabularyFile(secondChapter+"-6.png"), getVocabularyFile(secondChapter+"-6.mp3")));
            vocabulary.add(new VocabularyWord("des", "unbestimmter Artikel im Pl.; wird nicht übersetzt", secondChapter, 7, getVocabularyFile(secondChapter+"-7.png"), getVocabularyFile(secondChapter+"-7.mp3")));
            vocabulary.add(new VocabularyWord("le livre", "das Buch", secondChapter, 8, getVocabularyFile(secondChapter+"-8.png"), getVocabularyFile(secondChapter+"-8.mp3")));
            vocabulary.add(new VocabularyWord("la lampe", "die Lampe", secondChapter, 9, getVocabularyFile(secondChapter+"-9.png"), getVocabularyFile(secondChapter+"-9.mp3")));
            vocabulary.add(new VocabularyWord("l'étagère (f.)", "das Regal", secondChapter, 10, getVocabularyFile(secondChapter+"-10.png"), getVocabularyFile(secondChapter+"-10.mp3")));
            vocabulary.add(new VocabularyWord("la minichaîne", "die Mini-Stereoanlage", secondChapter, 11, getVocabularyFile(secondChapter+"-11.png"), getVocabularyFile(secondChapter+"-11.mp3")));
            vocabulary.add(new VocabularyWord("le lit", "das Bett", secondChapter, 12, getVocabularyFile(secondChapter+"-12.png"), getVocabularyFile(secondChapter+"-12.mp3")));
            vocabulary.add(new VocabularyWord("la chaise", "der Stuhl", secondChapter, 13, getVocabularyFile(secondChapter+"-13.png"), getVocabularyFile(secondChapter+"-13.mp3")));
            vocabulary.add(new VocabularyWord("ma chambre/ta chambre", "mein Zimmer/dein Zimmer", secondChapter, 14, getVocabularyFile(secondChapter+"-14.png"), getVocabularyFile(secondChapter+"-14.mp3")));
            vocabulary.add(new VocabularyWord("la chambre", "das Schlafzimmer", secondChapter, 15, getVocabularyFile(secondChapter+"-15.png"), getVocabularyFile(secondChapter+"-15.mp3")));
            vocabulary.add(new VocabularyWord("il y a", "es gibt", secondChapter, 16, getVocabularyFile(secondChapter+"-16.png"), getVocabularyFile(secondChapter+"-16.mp3")));
            vocabulary.add(new VocabularyWord("le poster", "das Poster", secondChapter, 17, getVocabularyFile(secondChapter+"-17.png"), getVocabularyFile(secondChapter+"-17.mp3")));
            vocabulary.add(new VocabularyWord("la photo", "das Foto/Bild", secondChapter, 18, getVocabularyFile(secondChapter+"-18.png"), getVocabularyFile(secondChapter+"-18.mp3")));
            vocabulary.add(new VocabularyWord("partout", "überall", secondChapter, 19, getVocabularyFile(secondChapter+"-19.png"), getVocabularyFile(secondChapter+"-19.mp3")));
            vocabulary.add(new VocabularyWord("le coin musique", "die Musikecke", secondChapter, 20, getVocabularyFile(secondChapter+"-20.png"), getVocabularyFile(secondChapter+"-20.mp3")));
            vocabulary.add(new VocabularyWord("le coin", "die Ecke", secondChapter, 21, getVocabularyFile(secondChapter+"-21.png"), getVocabularyFile(secondChapter+"-21.mp3")));
            vocabulary.add(new VocabularyWord("la musique", "die Musik", secondChapter, 22, getVocabularyFile(secondChapter+"-22.png"), getVocabularyFile(secondChapter+"-22.mp3")));
            vocabulary.add(new VocabularyWord("la guitare", "die Gitarre", secondChapter, 23, getVocabularyFile(secondChapter+"-23.png"), getVocabularyFile(secondChapter+"-23.mp3")));
            vocabulary.add(new VocabularyWord("le CD/les CD", "die CD", secondChapter, 24, getVocabularyFile(secondChapter+"-24.png"), getVocabularyFile(secondChapter+"-24.mp3")));
            vocabulary.add(new VocabularyWord("l'ordinateur (m.)", "der Computer", secondChapter, 25, getVocabularyFile(secondChapter+"-25.png"), getVocabularyFile(secondChapter+"-25.mp3")));
            vocabulary.add(new VocabularyWord("le hamac", "die Hängematte", secondChapter, 26, getVocabularyFile(secondChapter+"-26.png"), getVocabularyFile(secondChapter+"-26.mp3")));
            vocabulary.add(new VocabularyWord("la table", "der Tisch", secondChapter, 27, getVocabularyFile(secondChapter+"-27.png"), getVocabularyFile(secondChapter+"-27.mp3")));
            vocabulary.add(new VocabularyWord("la bédé/B.D./les B.D.", "der Comic", secondChapter, 28, getVocabularyFile(secondChapter+"-28.png"), getVocabularyFile(secondChapter+"-28.mp3")));
            vocabulary.add(new VocabularyWord("la collection", "die Sammlung", secondChapter, 29, getVocabularyFile(secondChapter+"-29.png"), getVocabularyFile(secondChapter+"-29.mp3")));
            vocabulary.add(new VocabularyWord("la figurine", "die Figur", secondChapter, 30, getVocabularyFile(secondChapter+"-30.png"), getVocabularyFile(secondChapter+"-30.mp3")));
            vocabulary.add(new VocabularyWord("Qu'est-ce qu'il y a?", "Was gibt es?", secondChapter, 31, getVocabularyFile(secondChapter+"-31.png"), getVocabularyFile(secondChapter+"-31.mp3")));
            vocabulary.add(new VocabularyWord("Qu'est-ce que...?", "Was...?", secondChapter, 32, getVocabularyFile(secondChapter+"-32.png"), getVocabularyFile(secondChapter+"-32.mp3")));
            vocabulary.add(new VocabularyWord("encore", "noch", secondChapter, 33, getVocabularyFile(secondChapter+"-33.png"), getVocabularyFile(secondChapter+"-33.mp3")));
            vocabulary.add(new VocabularyWord("la géo (fam.), la géographie", "die Geographie", secondChapter, 34, getVocabularyFile(secondChapter+"-34.png"), getVocabularyFile(secondChapter+"-34.mp3")));
            vocabulary.add(new VocabularyWord("la pierre", "der Stein", secondChapter, 35, getVocabularyFile(secondChapter+"-35.png"), getVocabularyFile(secondChapter+"-35.mp3")));
            vocabulary.add(new VocabularyWord("le globe", "der Globus", secondChapter, 36, getVocabularyFile(secondChapter+"-36.png"), getVocabularyFile(secondChapter+"-36.mp3")));

            //Unité 2 Volet 2
            vocabulary.add(new VocabularyWord("la salle de bains", "das Badezimmer", secondChapter, 37, getVocabularyFile(secondChapter+"-37.png"), getVocabularyFile(secondChapter+"-37.mp3")));
            vocabulary.add(new VocabularyWord("maman (f.)", "Mama", secondChapter, 38, getVocabularyFile(secondChapter+"-38.png"), getVocabularyFile(secondChapter+"-38.mp3")));
            vocabulary.add(new VocabularyWord("où", "wo, wohin", secondChapter, 39, getVocabularyFile(secondChapter+"-39.png"), getVocabularyFile(secondChapter+"-39.mp3")));
            vocabulary.add(new VocabularyWord("le shampoing", "das Shampoo", secondChapter, 40, getVocabularyFile(secondChapter+"-40.png"), getVocabularyFile(secondChapter+"-40.mp3")));
            vocabulary.add(new VocabularyWord("sur", "auf", secondChapter, 41, getVocabularyFile(secondChapter+"-41.png"), getVocabularyFile(secondChapter+"-41.mp3")));
            vocabulary.add(new VocabularyWord("à droite", "rechts", secondChapter, 42, getVocabularyFile(secondChapter+"-42.png"), getVocabularyFile(secondChapter+"-42.mp3")));
            vocabulary.add(new VocabularyWord("la cuisine", "die Küche", secondChapter, 43, getVocabularyFile(secondChapter+"-43.png"), getVocabularyFile(secondChapter+"-43.mp3")));
            vocabulary.add(new VocabularyWord("le biscuit", "der Keks", secondChapter, 44, getVocabularyFile(secondChapter+"-44.png"), getVocabularyFile(secondChapter+"-44.mp3")));
            vocabulary.add(new VocabularyWord("le placard", "der Wandschrank", secondChapter, 45, getVocabularyFile(secondChapter+"-45.png"), getVocabularyFile(secondChapter+"-45.mp3")));
            vocabulary.add(new VocabularyWord("à gauche", "links", secondChapter, 46, getVocabularyFile(secondChapter+"-46.png"), getVocabularyFile(secondChapter+"-46.mp3")));
            vocabulary.add(new VocabularyWord("le couloir", "der Flur", secondChapter, 47, getVocabularyFile(secondChapter+"-47.png"), getVocabularyFile(secondChapter+"-47.mp3")));
            vocabulary.add(new VocabularyWord("papa (m.)", "Papa", secondChapter, 48, getVocabularyFile(secondChapter+"-48.png"), getVocabularyFile(secondChapter+"-48.mp3")));
            vocabulary.add(new VocabularyWord("la clé", "der Schlüssel", secondChapter, 49, getVocabularyFile(secondChapter+"-49.png"), getVocabularyFile(secondChapter+"-49.mp3")));
            vocabulary.add(new VocabularyWord("l'appartement (m.)", "die Wohnung", secondChapter, 50, getVocabularyFile(secondChapter+"-50.png"), getVocabularyFile(secondChapter+"-50.mp3")));
            vocabulary.add(new VocabularyWord("ZAZ", "französische Sängerin", secondChapter, 51, getVocabularyFile(secondChapter+"-51.png"), getVocabularyFile(secondChapter+"-51.mp3")));
            vocabulary.add(new VocabularyWord("devant", "vor", secondChapter, 52, getVocabularyFile(secondChapter+"-52.png"), getVocabularyFile(secondChapter+"-52.mp3")));
            vocabulary.add(new VocabularyWord("derrière", "hinter", secondChapter, 53, getVocabularyFile(secondChapter+"-53.png"), getVocabularyFile(secondChapter+"-53.mp3")));
            vocabulary.add(new VocabularyWord("sous", "unter", secondChapter, 54, getVocabularyFile(secondChapter+"-54.png"), getVocabularyFile(secondChapter+"-54.mp3")));
            vocabulary.add(new VocabularyWord("la salle de séjour", "das Wohnzimmer", secondChapter, 55, getVocabularyFile(secondChapter+"-55.png"), getVocabularyFile(secondChapter+"-55.mp3")));
            vocabulary.add(new VocabularyWord("la télécommande", "die Fernbedienung", secondChapter, 56, getVocabularyFile(secondChapter+"-56.png"), getVocabularyFile(secondChapter+"-56.mp3")));
            vocabulary.add(new VocabularyWord("entre", "zwischen", secondChapter, 57, getVocabularyFile(secondChapter+"-57.png"), getVocabularyFile(secondChapter+"-57.mp3")));
            vocabulary.add(new VocabularyWord("la télé/la télévision", "der Fernseher", secondChapter, 58, getVocabularyFile(secondChapter+"-58.png"), getVocabularyFile(secondChapter+"-58.mp3")));

            //Unité 2 Volet 3
            vocabulary.add(new VocabularyWord("après", "nach", secondChapter, 59, getVocabularyFile(secondChapter+"-59.png"), getVocabularyFile(secondChapter+"-59.mp3")));
            vocabulary.add(new VocabularyWord("Elle rentre à la maison.", "Sie geht nach Hause. (3. Pers. Sg. von rentrer)", secondChapter, 60, getVocabularyFile(secondChapter+"-60.png"), getVocabularyFile(secondChapter+"-60.mp3")));
            vocabulary.add(new VocabularyWord("rentrer (à la maison)", "nach Hause gehen", secondChapter, 61, getVocabularyFile(secondChapter+"-61.png"), getVocabularyFile(secondChapter+"-61.mp3")));
            vocabulary.add(new VocabularyWord("les devoirs (m. pl.)", "die Hausaufgaben", secondChapter, 62, getVocabularyFile(secondChapter+"-62.png"), getVocabularyFile(secondChapter+"-62.mp3")));
            vocabulary.add(new VocabularyWord("elle écoute", "sie hört (3. Pers. Sg. von écouter)", secondChapter, 63, getVocabularyFile(secondChapter+"-63.png"), getVocabularyFile(secondChapter+"-63.mp3")));
            vocabulary.add(new VocabularyWord("écouter qn/qc", "etw. anhören, jdm zuhören, auf jdn hören", secondChapter, 64, getVocabularyFile(secondChapter+"-64.png"), getVocabularyFile(secondChapter+"-64.mp3")));
            vocabulary.add(new VocabularyWord("elle cherche", "sie sucht (3. Pers. Sg. von chercher)", secondChapter, 65, getVocabularyFile(secondChapter+"-65.png"), getVocabularyFile(secondChapter+"-65.mp3")));
            vocabulary.add(new VocabularyWord("chercher", "suchen", secondChapter, 66, getVocabularyFile(secondChapter+"-66.png"), getVocabularyFile(secondChapter+"-66.mp3")));
            vocabulary.add(new VocabularyWord("l'information (f.)", "die Information", secondChapter, 67, getVocabularyFile(secondChapter+"-67.png"), getVocabularyFile(secondChapter+"-67.mp3")));
            vocabulary.add(new VocabularyWord("Internet (m.)", "das Internet", secondChapter, 68, getVocabularyFile(secondChapter+"-68.png"), getVocabularyFile(secondChapter+"-68.mp3")));
            vocabulary.add(new VocabularyWord("elle chatte", "sie chattet (3. Pers. Sg. von chatter)", secondChapter, 69, getVocabularyFile(secondChapter+"-69.png"), getVocabularyFile(secondChapter+"-69.mp3")));
            vocabulary.add(new VocabularyWord("chatter", "chatten", secondChapter, 70, getVocabularyFile(secondChapter+"-70.png"), getVocabularyFile(secondChapter+"-70.mp3")));
            vocabulary.add(new VocabularyWord("le copain/la copine", "der/die Freund/in, der Kumpel", secondChapter, 71, getVocabularyFile(secondChapter+"-71.png"), getVocabularyFile(secondChapter+"-71.mp3")));
            vocabulary.add(new VocabularyWord("elle téléphone", "sie telefoniert (3. Pers. Sg. von téléphoner)", secondChapter, 72, getVocabularyFile(secondChapter+"-72.png"), getVocabularyFile(secondChapter+"-72.mp3")));
            vocabulary.add(new VocabularyWord("téléphoner", "telefonieren", secondChapter, 73, getVocabularyFile(secondChapter+"-73.png"), getVocabularyFile(secondChapter+"-73.mp3")));
            vocabulary.add(new VocabularyWord("elle rêve", "sie träumt (3. Pers. Sg. von rêver)", secondChapter, 74, getVocabularyFile(secondChapter+"-74.png"), getVocabularyFile(secondChapter+"-74.mp3")));
            vocabulary.add(new VocabularyWord("rêver", "träumen", secondChapter, 75, getVocabularyFile(secondChapter+"-75.png"), getVocabularyFile(secondChapter+"-75.mp3")));
            vocabulary.add(new VocabularyWord("Qu'est-ce qu'il y a?", "Was ist los?", secondChapter, 76, getVocabularyFile(secondChapter+"-76.png"), getVocabularyFile(secondChapter+"-76.mp3")));
            vocabulary.add(new VocabularyWord("Qu'est-ce que tu fais?", "Was machst du?", secondChapter, 77, getVocabularyFile(secondChapter+"-77.png"), getVocabularyFile(secondChapter+"-77.mp3")));
            vocabulary.add(new VocabularyWord("je travaille", "ich lerne, ich arbeite (1. Pers. Sg. von travailler)", secondChapter, 78, getVocabularyFile(secondChapter+"-78.png"), getVocabularyFile(secondChapter+"-78.mp3")));
            vocabulary.add(new VocabularyWord("travailler", "arbeiten, lernen", secondChapter, 79, getVocabularyFile(secondChapter+"-79.png"), getVocabularyFile(secondChapter+"-79.mp3")));
            vocabulary.add(new VocabularyWord("Tu m'énerves.", "Du nervst (mich)", secondChapter, 80, getVocabularyFile(secondChapter+"-80.png"), getVocabularyFile(secondChapter+"-80.mp3")));
            vocabulary.add(new VocabularyWord("maintenant", "jetzt", secondChapter, 81, getVocabularyFile(secondChapter+"-81.png"), getVocabularyFile(secondChapter+"-81.mp3")));
            vocabulary.add(new VocabularyWord("elle chante", "sie singt (3. Pers. Sg. von chanter)", secondChapter, 82, getVocabularyFile(secondChapter+"-82.png"), getVocabularyFile(secondChapter+"-82.mp3")));
            vocabulary.add(new VocabularyWord("chanter", "singen", secondChapter, 83, getVocabularyFile(secondChapter+"-83.png"), getVocabularyFile(secondChapter+"-83.mp3")));
            vocabulary.add(new VocabularyWord("tu joues", "du spielst (2. Pers. Sg. von jouer)", secondChapter, 84, getVocabularyFile(secondChapter+"-84.png"), getVocabularyFile(secondChapter+"-84.mp3")));
            vocabulary.add(new VocabularyWord("jouer (avec qn)", "(mit jmdm) spielen", secondChapter, 85, getVocabularyFile(secondChapter+"-85.png"), getVocabularyFile(secondChapter+"-85.mp3")));
            vocabulary.add(new VocabularyWord("Pas maintenant.", "Nicht jetzt.", secondChapter, 86, getVocabularyFile(secondChapter+"-86.png"), getVocabularyFile(secondChapter+"-86.mp3")));
            vocabulary.add(new VocabularyWord("s'il te plaît", "bitte (wenn du eine Person ansprichst, die du duzt)", secondChapter, 87, getVocabularyFile(secondChapter+"-87.png"), getVocabularyFile(secondChapter+"-87.mp3")));
            vocabulary.add(new VocabularyWord("Joue avec moi./Jouez avec moi.", "Spiel mit mir!/Spielt mit mir!", secondChapter, 88, getVocabularyFile(secondChapter+"-88.png"), getVocabularyFile(secondChapter+"-88.mp3")));
            vocabulary.add(new VocabularyWord("s'il vous plaît", "bitte (wenn du dich an mehrere Personen wendest oder jdn siezt)", secondChapter, 89, getVocabularyFile(secondChapter+"-89.png"), getVocabularyFile(secondChapter+"-89.mp3")));
            vocabulary.add(new VocabularyWord("regarder la télé", "fernsehen", secondChapter, 90, getVocabularyFile(secondChapter+"-90.png"), getVocabularyFile(secondChapter+"-90.mp3")));
            vocabulary.add(new VocabularyWord("toujours", "immer", secondChapter, 91, getVocabularyFile(secondChapter+"-91.png"), getVocabularyFile(secondChapter+"-91.mp3")));
            vocabulary.add(new VocabularyWord("d'accord", "einverstanden", secondChapter, 92, getVocabularyFile(secondChapter+"-92.png"), getVocabularyFile(secondChapter+"-92.mp3")));
            vocabulary.add(new VocabularyWord("la partie de cartes", "eine Runde Karten", secondChapter, 93, getVocabularyFile(secondChapter+"-93.png"), getVocabularyFile(secondChapter+"-93.mp3")));
            vocabulary.add(new VocabularyWord("la carte", "die Karte (hier: Spielkarte)", secondChapter, 94, getVocabularyFile(secondChapter+"-94.png"), getVocabularyFile(secondChapter+"-94.mp3")));
            vocabulary.add(new VocabularyWord("merci", "danke", secondChapter, 95, getVocabularyFile(secondChapter+"-95.png"), getVocabularyFile(secondChapter+"-95.mp3")));

            //Unité 2 Module 1
            vocabulary.add(new VocabularyWord("le rap", "der Rap", secondChapter, 96, getVocabularyFile(secondChapter+"-96.png"), getVocabularyFile(secondChapter+"-96.mp3")));
            vocabulary.add(new VocabularyWord("tout le monde", "alle", secondChapter, 97, getVocabularyFile(secondChapter+"-97.png"), getVocabularyFile(secondChapter+"-97.mp3")));
            vocabulary.add(new VocabularyWord("On y va!", "Los geht's!", secondChapter, 98, getVocabularyFile(secondChapter+"-98.png"), getVocabularyFile(secondChapter+"-98.mp3")));
            vocabulary.add(new VocabularyWord("parler", "sprechen", secondChapter, 99, getVocabularyFile(secondChapter+"-99.png"), getVocabularyFile(secondChapter+"-99.mp3")));
            vocabulary.add(new VocabularyWord("trop", "zu, zu sehr, zu viel", secondChapter, 100, getVocabularyFile(secondChapter+"-100.png"), getVocabularyFile(secondChapter+"-100.mp3")));
            vocabulary.add(new VocabularyWord("vite", "schnell", secondChapter, 101, getVocabularyFile(secondChapter+"-101.png"), getVocabularyFile(secondChapter+"-101.mp3")));
            vocabulary.add(new VocabularyWord("Je ne comprends pas.", "Ich verstehe (es) nicht", secondChapter, 102, getVocabularyFile(secondChapter+"-102.png"), getVocabularyFile(secondChapter+"-102.mp3")));
            vocabulary.add(new VocabularyWord("Vous pouvez répéter?", "Können Sie das wiederholen?, Könnt ihr das wiederholen? (wenn du dich an mehrere Personen wendest, die du duzt)", secondChapter, 103, getVocabularyFile(secondChapter+"-103.png"), getVocabularyFile(secondChapter+"-103.mp3")));
            vocabulary.add(new VocabularyWord("répéter (qc)", "(etw.) wiederholen, nachsprechen", secondChapter, 104, getVocabularyFile(secondChapter+"-104.png"), getVocabularyFile(secondChapter+"-104.mp3")));
            vocabulary.add(new VocabularyWord("fermer (qc)", "(etw.) schließen, zumachen", secondChapter, 105, getVocabularyFile(secondChapter+"-105.png"), getVocabularyFile(secondChapter+"-105.mp3")));
            vocabulary.add(new VocabularyWord("la tableau/les tableaux", "die (Schul-)Tafel", secondChapter, 106, getVocabularyFile(secondChapter+"-106.png"), getVocabularyFile(secondChapter+"-106.mp3")));
            vocabulary.add(new VocabularyWord("encore une fois", "noch einmal", secondChapter, 107, getVocabularyFile(secondChapter+"-107.png"), getVocabularyFile(secondChapter+"-107.mp3")));
            vocabulary.add(new VocabularyWord("ouvrez", "Öffnet! (hier: Macht ... auf!), Öffnen Sie! (Imperativ 2. Pers. Pl. von ouvrir (qc))", secondChapter, 108, getVocabularyFile(secondChapter+"-108.png"), getVocabularyFile(secondChapter+"-108.mp3")));
            vocabulary.add(new VocabularyWord("lisez", "Lest!, Lesen Sie! (Imperativ 2. Pers. Pl. von lire)", secondChapter, 109, getVocabularyFile(secondChapter+"-109.png"), getVocabularyFile(secondChapter+"-109.mp3")));
            vocabulary.add(new VocabularyWord("lire qc", "etw. lesen", secondChapter, 110, getVocabularyFile(secondChapter+"-110.png"), getVocabularyFile(secondChapter+"-110.mp3")));
            vocabulary.add(new VocabularyWord("le texte", "der Text", secondChapter, 111, getVocabularyFile(secondChapter+"-111.png"), getVocabularyFile(secondChapter+"-111.mp3")));
            vocabulary.add(new VocabularyWord("écrivez", "Schreibt!, Schreiben Sie! (Imperativ 2. Pers. Pl. von écrire)", secondChapter, 112, getVocabularyFile(secondChapter+"-112.png"), getVocabularyFile(secondChapter+"-112.mp3")));
            vocabulary.add(new VocabularyWord("écrire qc", "etw. schreiben", secondChapter, 113, getVocabularyFile(secondChapter+"-113.png"), getVocabularyFile(secondChapter+"-113.mp3")));
            vocabulary.add(new VocabularyWord("continuer", "weiter machen", secondChapter, 114, getVocabularyFile(secondChapter+"-114.png"), getVocabularyFile(secondChapter+"-114.mp3")));
            vocabulary.add(new VocabularyWord("la fenêtre", "das Fenster", secondChapter, 115, getVocabularyFile(secondChapter+"-115.png"), getVocabularyFile(secondChapter+"-115.mp3")));
            vocabulary.add(new VocabularyWord("la porte", "die Tür", secondChapter, 116, getVocabularyFile(secondChapter+"-116.png"), getVocabularyFile(secondChapter+"-116.mp3")));
            vocabulary.add(new VocabularyWord("le mot", "das Wort", secondChapter, 117, getVocabularyFile(secondChapter+"-117.png"), getVocabularyFile(secondChapter+"-117.mp3")));
            vocabulary.add(new VocabularyWord("la phrase", "der Satz", secondChapter, 118, getVocabularyFile(secondChapter+"-118.png"), getVocabularyFile(secondChapter+"-118.mp3")));

            vocabularyWordsRepository.saveAll(vocabulary);
            vocabularyWordsRepository.flush();

            final @NotNull List<@NotNull ExerciseTemplate> exerciseTemplates = new ArrayList<>();
            exerciseTemplates.add(new VocabularyOfChapterExerciseTemplate(
                    "Vokabelübung Unité 1",
                    firstChapter,
                    vocabulary.stream().filter(vocabularyWord -> vocabularyWord.getChapter().equals(firstChapter)).toList()
            ));

            exerciseTemplates.add(new VocabularyOfChapterExerciseTemplate(
                    "Vokabelübung Unité 2",
                    secondChapter,
                    vocabulary.stream().filter(vocabularyWord -> vocabularyWord.getChapter().equals(secondChapter)).toList()
            ));

            exerciseTemplatesRepository.saveAll(exerciseTemplates);
            exerciseTemplatesRepository.flush();

            final @NotNull Book frenchBook = new Book(
                    frenchbookIsbn,
                    "À plus ! - Französisch als 1. und 2. Fremdsprache - Bayern - Ausgabe 2017",
                    "Cornelsen Verlag",
                    exerciseTemplates,
                    vocabulary
            );
            booksRepository.save(frenchBook);
        }
    }

    public static byte@Nullable[] getVocabularyFile(@NotNull String filename) {
        try {
            @Nullable InputStream resourceInputStream = Main.class.getClassLoader().getResourceAsStream("vocabularyFiles/"+filename);
            if(resourceInputStream == null) return null;

            final byte[] resourceBytes;
            try (resourceInputStream) {
                resourceBytes = resourceInputStream.readAllBytes();
            }

            if(resourceBytes.length == 0) return null;
            return resourceBytes;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
