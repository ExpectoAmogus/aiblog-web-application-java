package com.expectoamogus.aiblog.models.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class SearchResult {
    private String kind;
    private Url url;
    private Queries queries;
    private List<Promotions> promotions;
    private Context context;
    private SearchInformation searchInformation;
    private Spelling spelling;
    private List<Item> items;

    // Add getters and setters

    public static SearchResult fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SearchResult.class);
    }

    @Getter
    @Setter
    @ToString
    public static class Url {
        private String type;
        private String template;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Queries {
        private List<Request> previousPage;
        private List<Request> request;

        private List<Request> nextPage;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Promotions {
        private String title;
        @SerializedName("htmlTitle")
        private String htmlTitle;
        private String link;
        private String displayLink;
        private List<BodyLines> bodyLines;
        private Image image;
    }

    @Getter
    @Setter
    @ToString
    public static class Image {
        private String source;
        private int width;
        private int height;
    }

    @Getter
    @Setter
    @ToString
    public static class BodyLines {
        private String title;
        @SerializedName("htmlTitle")
        private String htmlTitle;
        private String url;
        private String link;
    }

    @Getter
    @Setter
    @ToString
    public static class Request {
        private String title;
        private String totalResults;
        private String searchTerms;
        private int count;
        private int startIndex;
        private int startPage;
        private String language;
        private String inputEncoding;
        private String outputEncoding;
        private String safe;
        private String cx;
        private String sort;
        private String filter;
        private String gl;
        private String cr;
        private String googleHost;
        private String disableCnTwTranslation;
        private String hq;
        private String hl;
        private String siteSearch;
        private String siteSearchFilter;
        private String exactTerms;
        private String excludeTerms;
        private String linkSite;
        private String orTerms;
        private String relatedSite;
        private String dateRestrict;
        private String lowRange;
        private String highRange;
        private String fileType;
        private String rights;
        private String  searchType;
        private String imgSize;
        private String imgType;
        private String imgColorType;
        private String imgDominantColor;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Context {
        private String title;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class SearchInformation {
        private double searchTime;
        private String formattedSearchTime;
        private String totalResults;
        private String formattedTotalResults;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Spelling {
        private String correctedQuery;
        private String htmlCorrectedQuery;
    }

    @Getter
    @Setter
    @ToString
    public static class Item {
        private String kind;
        private String title;
        @SerializedName("htmlTitle")
        private String htmlTitle;
        private String link;
        private String displayLink;
        private String snippet;
        @SerializedName("htmlSnippet")
        private String htmlSnippet;
        private String cacheId;
        private String formattedUrl;
        @SerializedName("htmlFormattedUrl")
        private String htmlFormattedUrl;
        private Pagemap pagemap;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Pagemap {
        @SerializedName("cse_thumbnail")
        private List<CseThumbnail> cseThumbnailList;
        @SerializedName("imageobject")
        private List<ImageObject> imageObjectList;
        private List<Person> personList;
        @SerializedName("interactioncounter")
        private List<InteractionCounter> interactionCounterList;
        private List<Metatag> metatagList;
        @SerializedName("videoobject")
        private List<VideoObject> videoObjectList;
        private List<Collection> collectionList;
        private List<Creativework> creativeWorkList;
        @SerializedName("cse_image")
        private List<CseImage> cseImageList;
        @SerializedName("socialmediaposting")
        private List<SocialMediaPosting> socialMediaPostingList;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class CseThumbnail {
        private String src;
        private String width;
        private String height;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class ImageObject {
        private String contenturl;
        private String width;
        private String caption;
        private String thumbnailurl;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Person {
        private String identifier;
        private String givenname;
        private String additionalname;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class InteractionCounter {
        private String userinteractioncount;
        private String interactiontype;
        private String name;
        private String url;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Metatag {
        @SerializedName("og:image")
        private String ogImage;
        @SerializedName("theme-color")
        private String themeColor;
        @SerializedName("og:type")
        private String ogType;
        @SerializedName("og:site_name")
        private String ogSiteName;
        @SerializedName("al:ios:app_name")
        private String alIosAppName;
        @SerializedName("apple-mobile-web-app-title")
        private String appleMobileWebAppTitle;
        @SerializedName("og:title")
        private String ogTitle;
        @SerializedName("al:android:package")
        private String alAndroidPackage;
        @SerializedName("al:ios:url")
        private String alIosUrl;
        @SerializedName("og:description")
        private String ogDescription;
        @SerializedName("al:ios:app_store_id")
        private String alIosAppStoreId;
        @SerializedName("facebook-domain-verification")
        private String facebookDomainVerification;
        @SerializedName("al:android:url")
        private String alAndroidUrl;
        @SerializedName("fb:app_id")
        private String fbAppId;
        @SerializedName("apple-mobile-web-app-status-bar-style")
        private String appleMobileWebAppStatusBarStyle;
        private String viewport;
        @SerializedName("mobile-web-app-capable")
        private String mobileWebAppCapable;
        @SerializedName("og:url")
        private String ogUrl;
        @SerializedName("al:android:app_name")
        private String alAndroidAppName;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class VideoObject {
        private String duration;
        private String embedurl;
        private String contenturl;
        private String uploaddate;
        private String name;
        private String description;
        private String caption;
        private String thumbnailurl;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Collection {
        private String name;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class Creativework {
        private String name;
        private String url;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class CseImage {
        private String src;

        // Add getters and setters
    }

    @Getter
    @Setter
    @ToString
    public static class SocialMediaPosting {
        private String identifier;
        private String commentcount;
        private String articlebody;
        private String position;
        private String datecreated;
        private String datepublished;
        private String url;

        // Add getters and setters
    }
}

