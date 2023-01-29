package com.innovamates.learnenglish.utils

import com.innovamates.learnenglish.data.models.Sentence
import com.innovamates.learnenglish.data.database.videoitem.DbVideoItem

object FakeVideoItemGenerator {
    fun getVideoItems(id: Int): List<DbVideoItem> {
        val list = ArrayList<DbVideoItem>()

        val listOfSentences = ArrayList<Sentence>()

        val sentence1 = Sentence(1, "master master", "", 2, 4)
        val sentence2 = Sentence(2, "hmm", "", 4, 5)
        val sentence3 = Sentence(3, "i have it it's up it's very bad news", "", 5, 10)
        val sentence4 =
            Sentence(4, "uh shifu there is just news there is no good or bad", "", 10, 14)
        val sentence5 = Sentence(5, "master your vision your vision was right", "", 14, 18)
        val sentence6 = Sentence(6, "Tai lung has broken out of prison", "", 18, 21)
        val sentence7 = Sentence(7, "his on his way", "", 21, 22)
        val sentence8 = Sentence(8, "That is bad news", "", 22, 27)
        val sentence9 =
            Sentence(9, "if you do not believe that the Dragon Warrior can stop him.", "", 27, 31)
        val sentence10 =
            Sentence(10, "The panda? Master, that panda is not the Dragon Warrior.", "", 31, 36)
        val sentence11 = Sentence(11, "He wasn't meant to be here! It was an accident.", "", 36, 38)
        val sentence12 = Sentence(12, "There are no accidents.", "", 38, 41)
        val sentence13 = Sentence(13, "Yes, I know. You've said that already.", "", 41, 46)
        val sentence14 = Sentence(14, "Twice. Well, that was no accident, either.", "", 46, 50)

        listOfSentences.add(sentence1)
        listOfSentences.add(sentence2)
        listOfSentences.add(sentence3)
        listOfSentences.add(sentence4)
        listOfSentences.add(sentence5)
        listOfSentences.add(sentence6)
        listOfSentences.add(sentence7)
        listOfSentences.add(sentence8)
        listOfSentences.add(sentence9)
        listOfSentences.add(sentence10)
        listOfSentences.add(sentence11)
        listOfSentences.add(sentence12)
        listOfSentences.add(sentence13)
        listOfSentences.add(sentence14)

        val dbVideoItem1 = DbVideoItem(id,
            "Kung Fu Panda",
            "Oogway Ascends",
            "https://www.youtube.com/watch?v=1",
            "hYAQtEs2Img",
            0,
            50,
            listOfSentences)

        list.add(dbVideoItem1)

        return list
    }
}
