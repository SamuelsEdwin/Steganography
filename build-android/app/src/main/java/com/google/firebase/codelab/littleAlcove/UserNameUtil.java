//This program is a modification of the codelab friendly chat tutorial
package com.google.firebase.codelab.littleAlcove;

import java.util.Random;
import java.util.UUID;

public class UserNameUtil {

    private static final String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
    private static final String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};

    private String name;
    private String uuid;
    private String userName;

    public UserNameUtil() {
        generateNewName();
        generateNewUUID();
        userName=name+"@"+uuid;
    }
    private String generateNewName() {
        Random random = new Random();
        name = adjs[random.nextInt(adjs.length-1)] + "-"+nouns[random.nextInt(nouns.length-1)];
        return name;
    }

    private String generateNewUUID() {
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUserName() {
        return userName;
    }


}
