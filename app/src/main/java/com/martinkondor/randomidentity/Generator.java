package com.martinkondor.randomidentity;

import android.content.res.Resources;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

class Generator {

    protected List<String> lastNames = new ArrayList<String>();
    protected List<String> maleFirstNames = new ArrayList<String>();
    protected List<String> femaleFirstNames = new ArrayList<String>();

    // Country name: country phone code
    protected Map<String, String> countries = new HashMap<>();

    // Country name: city list
    protected Map<String, List<String>> cities = new HashMap<>();

    // City name: county name
    protected Map<String, String> cityCounties = new HashMap<>();

    protected String[] months = new String[] {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
    };
    protected int[] monthDays = new int[] {31, 28, 31, 29, 31, 30, 29, 31, 30, 31, 30, 31};
    protected String[] emailDomains = new String[] {
            "@armyspy.com", "@dayrep.com", "@einrot.com", "@fleckens.hu", "@gustr.com",
            "@jourrapide.com", "@rhyta.com", "@superrito.com", "@teleworm.us"
    };

    public Generator(Resources res) {
        // Load lists
        try {
            InputStreamReader isr = new InputStreamReader(res.openRawResource(R.raw.male_names));
            BufferedReader fr = new BufferedReader(isr);
            String line = null;
            do {
                line = fr.readLine();
                if (line == null) break;
                maleFirstNames.add(line.split(",")[1]);
            } while (line != null);
            isr.close();
            fr.close();

            isr = new InputStreamReader(res.openRawResource(R.raw.female_names));
            fr = new BufferedReader(isr);
            line = null;
            do {
                line = fr.readLine();
                if (line == null) break;
                femaleFirstNames.add(line.split(",")[1]);
            } while (line != null);
            isr.close();
            fr.close();

            isr = new InputStreamReader(res.openRawResource(R.raw.last_names));
            fr = new BufferedReader(isr);
            line = null;
            do {
                line = fr.readLine();
                if (line == null) break;
                lastNames.add(line.split(",")[0]);
            } while (line != null);
            isr.close();
            fr.close();

            isr = new InputStreamReader(res.openRawResource(R.raw.country_phone_codes));
            fr = new BufferedReader(isr);
            line = null;
            do {
                line = fr.readLine();
                if (line == null) break;
                String[] data = line.split(",");
                countries.put(data[2], data[1]);
            } while (line != null);
            isr.close();
            fr.close();

            isr = new InputStreamReader(res.openRawResource(R.raw.cities));
            fr = new BufferedReader(isr);
            line = null;
            do {
                line = fr.readLine();
                if (line == null) break;
                String[] data = line.split(",");

                // If there is no such country in the map
                if (!cities.keySet().contains(data[1])) {
                    List<String> temp = new ArrayList<String>();
                    temp.add(data[0]);
                    cities.put(data[1], temp);
                }
                else {
                    cities.get(data[1]).add(data[0]);
                }
                cityCounties.put(data[0], data[2]);
            } while (line != null);
            isr.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int getInt(int max) {
        return new Random().nextInt(max);
    }

    protected int getInt(int max, int min) {
        return new Random().nextInt(max - min) + min;
    }

    protected String[] getCountryNames(Resources res) {
        List<String> l = new ArrayList<String>(countries.keySet());
        Collections.sort(l);
        return (String[]) l.toArray(new String[l.size()]);
    }

    protected String getPhone() {
        return getInt(999, 10) + "-"
                + getInt(999, 100) + "-"
                + getInt(9999, 1000);
    }

    protected String getCountryCode(String country) {
        return "+" + countries.get(country);
    }

    protected String getName(boolean isMale) {
        return isMale ? choice(maleFirstNames) : choice(femaleFirstNames);
    }

    protected String getFullName(boolean isMale) {
        return getName(isMale) + " " + choice(lastNames);
    }

    protected String getCity(String country) {
        if (cities.keySet().contains(country)) {
            String city = choice(cities.get(country));
            return city + ",\n" + cityCounties.get(city);
        }
        return "None";
    }

    protected String getAddress() {
        return getFullName(getInt(100) % 2 == 0) + " " + getInt(1000);
    }

    protected String getBirthday() {
        int i = getInt(months.length - 1);
        return months[i] + " " + getInt(monthDays[i]) + ", " + getInt(2022, 1947);
    }

    protected String getEmail(String fullName) {
        String[] name = fullName.toLowerCase().split(" ");
        int seed = getInt(100);

        if (seed < 9 && name[1].length() > 3) {
            return name[0] + name[1].charAt(0) + name[1].charAt(1) + name[1].charAt(2) + choice(emailDomains);
        }
        else if (seed < 29) {
            return name[getInt(10)%2==0 ? 1 : 0] + getInt(9999, 10) + choice(emailDomains);
        }
        else if (seed < 59) {
            return name[getInt(10)%2==0 ? 1 : 0] + getInt(9999, 10) + choice(emailDomains);
        }
        else if (seed < 89) {
            return name[getInt(10)%2==0 ? 1 : 0] + getInt(9999, 10) + choice(emailDomains);
        }
        return name[0] + name[1] + getInt(99) + choice(emailDomains);
    }

    protected String getUsername(String fullName) {
        String[] name = fullName.toLowerCase().split(" ");
        int seed = getInt(100);
        if (seed < 9 && name[1].length() > 3) {
            return name[0] + name[1].charAt(0) + name[1].charAt(1) + name[1].charAt(2);
        }
        else if (seed < 29) {
            return name[getInt(10)%2==0 ? 1 : 0] + getInt(9999, 10);
        }
        else if (seed < 59) {
            return name[getInt(10)%2==0 ? 1 : 0] + getInt(9999, 10);
        }
        else if (seed < 89) {
            return name[getInt(10)%2==0 ? 1 : 0] + getInt(9999, 10);
        }
        return name[0] + name[1] + getInt(99);
    }

    protected String getPassword() {
        String result = "";
        for (int i = 0; i < getInt(16, 8); i++) {
            result += getInt(9);
        }
        return result;
    }

    private String choice(List<String> l) {
        return l.get(getInt(l.size()));
    }

    private String choice(String[] l) {
        return l[getInt(l.length - 1)];
    }
}
