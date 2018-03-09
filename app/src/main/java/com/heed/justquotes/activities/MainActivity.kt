package com.heed.justquotes.activities

import android.os.Bundle
import com.heed.justquotes.R
import com.heed.justquotes.fragments.QuoteCategoryFragment
import com.heed.justquotes.fragments.QuoteOfTheDayFragment
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val _tag: String = this@MainActivity.javaClass.simpleName

    private val _menuQuoteOfTheDay: String = "Quote Of The Day"
    private val _menuRandom: String = "Random"
    private val _menuFamous: String = "Famous"
    private val _menuMovies: String = "Movies"

    private var drawer: Drawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(this@MainActivity.toolbar)
        setUpDrawer(savedInstanceState)

        showQuoteOfTheDay()
    }

    private fun setUpDrawer(savedInstanceState: Bundle?) {
        val accountHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.header)
                //.addProfiles(ProfileDrawerItem().withName("Anonymous"))
                .withSelectionListEnabledForSingleProfile(false)
                .build()

        val popularCategories = resources.getStringArray(R.array.popular_quotes_categories)
        /**
         * Equivalent to:
         * val popularDrawerItems = ArrayList<IDrawerItem<*, *>>()
         * for (category in popularCategories)
         * popularDrawerItems.add(SecondaryDrawerItem().withName(category))
         */
        val popularDrawerItems = popularCategories.map { it: String? -> SecondaryDrawerItem().withName(it) }

        val otherCategories = resources.getStringArray(R.array.other_quotes_categories)
        val otherDrawerItems = otherCategories.map { SecondaryDrawerItem().withName(it) }

        drawer = DrawerBuilder().withActivity(this@MainActivity)
                .withToolbar(this@MainActivity.toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        PrimaryDrawerItem().withName("Quote Of The Day").withIcon(FontAwesome.Icon.faw_lightbulb_o),
                        SectionDrawerItem().withName("Categories"),
                        ExpandableDrawerItem().withName("Popular").withIcon(FontAwesome.Icon.faw_fire).withSelectable(false).withSubItems(
                                popularDrawerItems
                        )
                        // TODO: Re-enable when endpoints have been sorted out
                        /*ExpandableDrawerItem().withName("Other").withIcon(FontAwesome.Icon.faw_quote_right).withSelectable(false).withSubItems(
                                otherDrawerItems
                        )*/
                )
                .withOnDrawerItemClickListener({ _, _, iDrawerItem ->
                    if (iDrawerItem is PrimaryDrawerItem) {
                        when (iDrawerItem.name.toString()) {
                            _menuQuoteOfTheDay -> showQuoteOfTheDay()
                        }
                    }

                    if (iDrawerItem is SecondaryDrawerItem) {
                        when (iDrawerItem.name.toString()) {
                            _menuRandom -> showQuoteFromCategory(null)
                            _menuFamous -> showQuoteFromCategory("famous")
                            _menuMovies -> showQuoteFromCategory("movies")
                        }
                    }
                    false
                })
                .withOnDrawerNavigationListener {//this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                    //if the back arrow is shown. close the activity
                    _ ->
                    this@MainActivity.finish()
                    // return true if event is consumed
                    true
                }
                /*.addStickyDrawerItems(
                        SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog),
                        SecondaryDrawerItem().withName("Help").withIcon(FontAwesome.Icon.faw_question),
                        SecondaryDrawerItem().withName("Contact Me").withIcon(FontAwesome.Icon.faw_bullhorn)
                )*/
                .withSavedInstance(savedInstanceState)
                .build()
    }

    @Suppress("NAME_SHADOWING")
    override fun onSaveInstanceState(outState: Bundle?) {
        var outState = outState
        // add the values which need to be saved from the drawer to the bundle
        outState = drawer!!.saveInstanceState(outState!!)
        super.onSaveInstanceState(outState)
    }

    private fun showQuoteOfTheDay() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, QuoteOfTheDayFragment())
                .commit()
    }

    private fun showQuoteFromCategory(category: String?) {
        val bundle = Bundle()
        bundle.putString("category", category)
        val quoteCategoryFragment = QuoteCategoryFragment()
        quoteCategoryFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, quoteCategoryFragment)
                .commit()
    }
}
