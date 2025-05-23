package com.aforeapps.learnenglish.utils

import com.aforeapps.learnenglish.data.models.Category
import com.aforeapps.learnenglish.data.models.CategoryData
import com.aforeapps.learnenglish.data.models.Sentence
import com.aforeapps.learnenglish.data.models.SubCategory
import com.aforeapps.learnenglish.data.models.VideoItem

object FakeVideoItemGenerator {
    fun getVideoItems(id: Int): List<VideoItem> {
        val list = ArrayList<VideoItem>()

        val listOfSentences = ArrayList<Sentence>()

        // Update Sentence constructor to match data class (id, words, startTimeSec, endTimeSec, isPlaying, audioTrack)
        val sentence1 = Sentence(1, "master master", 2, 4)
        val sentence2 = Sentence(2, "hmm", 4, 5)
        val sentence3 = Sentence(3, "i have it it's up it's very bad news", 5, 10)
        val sentence4 = Sentence(4, "uh shifu there is just news there is no good or bad", 10, 14)
        val sentence5 = Sentence(5, "master your vision your vision was right", 14, 18)
        val sentence6 = Sentence(6, "Tai lung has broken out of prison", 18, 21)
        val sentence7 = Sentence(7, "his on his way", 21, 22)
        val sentence8 = Sentence(8, "That is bad news", 22, 27)
        val sentence9 =
            Sentence(9, "if you do not believe that the Dragon Warrior can stop him.", 27, 31)
        val sentence10 =
            Sentence(10, "The panda? Master, that panda is not the Dragon Warrior.", 31, 36)
        val sentence11 = Sentence(11, "He wasn't meant to be here! It was an accident.", 36, 38)
        val sentence12 = Sentence(12, "There are no accidents.", 38, 41)
        val sentence13 = Sentence(13, "Yes, I know. You've said that already.", 41, 46)
        val sentence14 = Sentence(14, "Twice. Well, that was no accident, either.", 46, 50)

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

        // Update VideoItem constructor to match data class
        val videoItem = VideoItem(
            category_id = 1,
            created_at = "2023-01-01T00:00:00Z",
            description = "Oogway Ascends",
            endTimeSec = 50,
            full_url = "https://www.youtube.com/watch?v=1",
            id = id,
            is_active = 1,
            startTimeSec = 0,
            subcategory_id = 1,
            thumbnail_url = "https://img.youtube.com/vi/hYAQtEs2Img/0.jpg",
            title = "Kung Fu Panda",
            updated_at = "2023-01-01T00:00:00Z",
            video_url = "https://www.youtube.com/watch?v=1",
            youtube_id = "hYAQtEs2Img",
            sentences = listOfSentences
        )

        list.add(videoItem)

        return list
    }

    fun getFakeCategories(): CategoryData {
        val subCategories = listOf(
            SubCategory(
                created_at = "2024-03-20",
                id = 1,
                is_subcategory = 1,
                name = "Basic",
                order = 1,
                parent_id = 1,
                updated_at = "2024-03-20",
                thumbnail_url = "https://example.com/thumb1.jpg",
                thumbnail_url1 = "https://example.com/thumb1_1.jpg",
                thumbnail_url2 = "https://example.com/thumb1_2.jpg",
                thumbnail_url3 = "https://example.com/thumb1_3.jpg"
            ),
            SubCategory(
                created_at = "2024-03-20",
                id = 2,
                is_subcategory = 1,
                name = "Advanced",
                order = 2,
                parent_id = 1,
                updated_at = "2024-03-20",
                thumbnail_url = "https://example.com/thumb2.jpg",
                thumbnail_url1 = "https://example.com/thumb2_1.jpg",
                thumbnail_url2 = "https://example.com/thumb2_2.jpg",
                thumbnail_url3 = "https://example.com/thumb2_3.jpg"
            )
        )

        val categories = listOf(
            Category(
                created_at = "2024-03-20",
                id = 1,
                is_subcategory = 0,
                name = "Beginner",
                order = 1,
                parent_id = 1,
                sub_categories = subCategories,
                updated_at = "2024-03-20"
            ),
            Category(
                created_at = "2024-03-20",
                id = 2,
                is_subcategory = 0,
                name = "Intermediate",
                order = 2,
                parent_id = 1,
                sub_categories = subCategories,
                updated_at = "2024-03-20"
            )
        )

        return CategoryData(
            categories = categories,
            error = "",
            success = true
        )
    }
}
