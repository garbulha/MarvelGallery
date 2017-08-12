package com.sample.marvelgallery.view.character

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import com.sample.marvelgallery.R
import com.sample.marvelgallery.model.MarvelCharacter
import com.sample.marvelgallery.view.common.extra
import com.sample.marvelgallery.view.common.getIntent
import com.sample.marvelgallery.view.common.loadImage
import kotlinx.android.synthetic.main.activity_character_profile.*

class CharacterProfileActivity : AppCompatActivity() {

    val character: MarvelCharacter by extra(CHARACTER_ARG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_profile)
        setUpToolbar()
        supportActionBar?.title = character.name
        descriptionView.text = character.description
        occurrencesView.text = Html.fromHtml(makeOccurrencesText())
        headerView.loadImage(character.imageUrl, centerCropped = true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        item.itemId == android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun makeOccurrencesText(): String {
        var occurrencesText = ""

        fun addListIfNotEmpty(introductionTextId: Int, list: List<String>) {
            if (list.isEmpty()) return
            val introductionText = getString(introductionTextId)
            val htmlList = list.toHtmlList()
            occurrencesText += "$introductionText $htmlEnter $htmlList $htmlEnter"
        }

        addListIfNotEmpty(R.string.occurrences_comics_list_introduction, character.comics)
        addListIfNotEmpty(R.string.occurrences_series_list_introduction, character.series)
        addListIfNotEmpty(R.string.occurrences_stories_list_introduction, character.stories)
        addListIfNotEmpty(R.string.occurrences_events_list_introduction, character.events)

        return occurrencesText
    }

    companion object {
        private const val htmlPoint = "&#8226;"
        private const val htmlEnter = "<br/>"
        private const val CHARACTER_ARG = "com.naxtlevelofandroiddevelopment.marvelgallery.presentation.heroprofile.CharacterArgKey"

        fun start(context: Context, character: MarvelCharacter) {
            val intent = context
                    .getIntent<CharacterProfileActivity>() // 1
                    .apply { putExtra(CHARACTER_ARG, character) }
            context.startActivity(intent)
        }

        private fun List<String>.toHtmlList(): String = fold("") { acc, item -> "$acc$htmlPoint $item $htmlEnter" }
    }
}
