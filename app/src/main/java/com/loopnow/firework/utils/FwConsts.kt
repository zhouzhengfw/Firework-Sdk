package com.loopnow.firework.utils

object FwConsts {
    const val TOKEN = "token"
    const val POSITION = "poisition"
    const val COUNT_OF_VIDEOS_PLAYED = "count_of_videos_played"
    const val APP_ID = "app_id"
    const val PAGE_TYPE = "page_type"
    const val BUNDLE_ID = "bundle_id"
    const val FEED_ID = "feed_id"
    const val FEED_TYPE = "feed_type"
    const val FEED_PARAM = "feed_param"
    const val GUEST_ID = "guest_id"
    const val TOKEN_RECEIVED_TIME = "token_received_time"

    //https://jerry.ngrok.io

    //"https://sdk.sandbox.fireworktv.com"

    const val  HOST_STAGING =  "https://staging.fireworktv.com" //"https://ad-videos-api.sandbox.fireworktv.com"
    // const val HOST_STAGING = "https://channel-personalized-feed.sandbox.fireworktv.com"

    // "https://naboo.ngrok.io" //"https://tag-category.sandbox.fireworktv.com"
    const val  SESSION_URL_STAGING = "$HOST_STAGING/embed/sessions"
    const val  REFRESH_SESSION_URL_STAGING = "$HOST_STAGING/embed/sessions/refresh"

    const val HOST_PRODUCTION =   "https://api.fw.tv"
    const val  SESSION_URL_PRODUCTION = "$HOST_PRODUCTION/embed/sessions"
    const val  REFRESH_SESSION_URL_PRODUCTION = "$HOST_PRODUCTION/embed/sessions/refresh"


    //const val HOST_STAGING = "https://staging.fireworktv.com"
    //const val  SESSION_URL_STAGING = HOST_STAGING + "/embed/sessions"


    const val PLAY_SEG_PLAYING = "playing"
    const val PLAY_SEG_PAUSED = "stopped"
    const val OFF_SCREEN_PAGE_LIMIT = 1
    const val VIDEO_FEED = "video_feed"
    const val PLAY_FEED = "play_feed"
    const val CURRENT_POSITION = "current_position"
    const val VIEW_ID = "view_id"
    const val ENABLE_SHARE = "enable_share"
    const val ENABLE_PLAYER_FULL_SCREEN = "enable_player_full_screen"
    const val ACTION_URL = "action_url"
    const val REDIRECT_URI = "redirect_uri"
    const val AUTO_PLAY_NEXT_VIDEO = "auto_play_next_video"

    const val PIXEL_TWO_PRODUCTION_URL = "https://p2.fwpixel.com/"
    const val PIXEL_TWO_STAGING_URL = "https://p2-staging.fwpixel.com/"

    //const val PIXEL_ONE_PRODUCTION_URL = "https://p1.fwpixel.com/"
    //const val PIXEL_ONE_STAGING_URL = "https://p1-staging.fwpixel.com/"

    var PLAY_SEGMENTS_URL = ""
    var VISITOR_EVENTS_URL = PIXEL_TWO_PRODUCTION_URL

    //const val OAUTH_URL = "https://fireworktv.com/oauth/token"
    const val OAUTH_URL = "/oauth/token"
    const val PLAY_SEGMENTS = "play_segments"
    const val VIDEO_EVENTS = "video_events"
    const val VISITOR_EVENTS = "visitor_events"

    const val REFRESH_TOKEN = "refreh_token"
    const val ACCESS_TOKEN = "access_token"
    const val CLIENT_ID = "client_id"

    const val HAS_DISPLAYED_TOOLTIP = "has_displayed_tooltip"

    //To do: move this into String with localizations.
    const val CHANNEL_NOT_FOUND_TOAST_TEXT = "Invalid channel_id. Please enter a valid channel_id and try again"


    //Share preference
    const val USER_LOGIN_ACCESS_TOKEN = "access_token"
    const val USER_LOGIN_REFRESH_TOKEN = "refresh_token"
    const val USER_LOGIN_REFRESH_TOKEN_EXPIRES_IN = "refresh_token_expires_in"
    const val USER_LOGIN_CREATED_AT = "created_at"

    const val IMA_PREROLL = "preroll"
    const val IMA_INSTERTIAL = "ima_interstitial"

}