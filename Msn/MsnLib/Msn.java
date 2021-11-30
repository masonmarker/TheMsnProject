package MsnLib;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Includes like 639487639486 methods Java's library should already have.
 * 
 * Method descriptions containing "(WIP)" indicates that specific method is still in experimental
 * stages, using them could cause errors.
 * 
 * @author Mason Marker
 * @version 0.1.5.3.5 - 10/25/2021
 */
public class Msn {

  // true = prints the return value after method execution if possible
  // false = no information printed after method execution
  private static boolean verbose = false;

  // Used in array directional methods
  private static final int intDirectionalConstant = Integer.MAX_VALUE;
  private static final double doubleDirectionalConstant = Double.MAX_VALUE;
  private static final char charDirectionalConstant = Character.MAX_VALUE;

  // ----------------------------VERBOSITY-------------------------------------

  /**
   * Sets verbosity for the MSN class.
   * 
   * @param setVerbose the verbosity
   * @since 0.1.0.0.0
   */
  public static void setVerbosity(boolean setVerbose) {
    if (setVerbose)
      verbose = true;
    else
      verbose = false;
  }

  // -----------------------------ALPHABET-------------------------------------

  /**
   * Returns the String representation of the English alphabet.
   * 
   * @return the alphabet
   * @since 0.1.4.0.0
   */
  public static String alphabet() {
    if (verbose)
      println("[+] retrieved alphabet");
    return String.valueOf(alphabetArray());
  }

  /**
   * Returns the array representation of the English alphabet.
   * 
   * @return the alphabet
   * @since 0.1.4.0.0
   */
  public static char[] alphabetArray() {
    if (verbose)
      println("[+] retrieved alphabet");
    return new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
        'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
  }

  /**
   * Gets all vowels in the English alphabet.
   * 
   * @return vowels
   * @since 0.1.4.0.0
   */
  public static char[] vowels() {
    if (verbose)
      println("[+] retrieved vowels");
    return new char[] {'a', 'e', 'i', 'o', 'u', 'y'};
  }

  /**
   * Gets the consonants that exist in the English alphabet.
   * 
   * @return the consonants
   * @since 0.1.4.0.0
   */
  public static char[] consonants() {
    if (verbose)
      println("[+] retrieved vowels");
    return new char[] {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's',
        't', 'v', 'w', 'x', 'z'};
  }

  /**
   * Checks whether the specified char is a vowel.
   * 
   * @param c the char
   * @return whether the char is a vowel or not
   * @since 0.1.5.3.0
   */
  public static boolean isVowel(char c) {
    return contains(vowels(), c);
  }

  /**
   * Checks whether the specified char is a consonant.
   * 
   * @param c the char
   * @return whether the char is a consonant or not
   * @since 0.1.5.3.0
   */
  public static boolean isConsonant(char c) {
    return contains(consonants(), c);
  }

  // ----------------------------CONSOLE--------------------------------------

  /**
   * Prints a String.
   * 
   * @param s the String to print
   * @since 0.1.0.0.0
   */
  public static void p(String s) {
    System.out.print(s);
  }

  /**
   * Prints a String.
   * 
   * @param s the String to print
   * @since 0.1.0.0.0
   */
  public static void println(String s) {
    System.out.println(s);
  }

  /**
   * Prints a String with a "box" around it.
   * 
   * @param s String s
   * @since 0.1.5.3.3
   */
  public static void printboxed(String s) {
    println(boxed(s));
  }

  /**
   * Pauses runtime for a certain amount of milliseconds.
   * 
   * @param waitTime the time to wait
   * @throws InterruptedException
   * @since 0.1.0.0.0
   */
  public static void wait(int waitTime, String timeunit) throws InterruptedException {
    if (timeunit.equals("ms"))
      TimeUnit.MILLISECONDS.sleep(waitTime);
    else if (timeunit.equals("s"))
      TimeUnit.SECONDS.sleep(waitTime);
    else if (timeunit.equals("m"))
      TimeUnit.MINUTES.sleep(waitTime);
    else if (timeunit.equals("ns"))
      TimeUnit.NANOSECONDS.sleep(waitTime);
    else
      TimeUnit.MILLISECONDS.sleep(waitTime);
  }

  // ----------------------------BINARY---------------------------------------

  /**
   * Returns the binary representation of the passed number.
   * 
   * @param num the number to use
   * @return binary representation of 'num'
   * @since 0.1.0.1.4
   */
  public static String binary(int num) {
    if (verbose)
      println("[+] binary rep of " + num + " is " + Integer.toBinaryString(num));
    return Integer.toBinaryString(num);
  }

  /**
   * Returns binary representation with base expansion.
   * 
   * @param num the number to use
   * @param base the base expantion to use
   * @return bin the String representation
   * @since 0.1.0.1.4
   */
  public static String binary(int num, int base) {
    String bin = "";
    int nextNum = num;
    for (int i = 0; i < base; i++) {
      bin += nextNum % base;
      nextNum = nextNum / base;
    }
    StringBuilder sb = new StringBuilder(bin).reverse();
    if (verbose)
      println("[+] binary rep of " + num + " base " + base + " is " + sb.toString());
    return sb.toString();
  }

  // ----------------------------CONTAINS-------------------------------------

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static <T> boolean contains(T[] array, T check) {
    for (int i = 0; i < array.length; i++)
      if (check.equals(array[i]))
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(int[] array, int check) {
    for (int i = 0; i < array.length; i++)
      if (check == array[i])
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(double[] array, double check) {
    for (int i = 0; i < array.length; i++)
      if (check == array[i])
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(boolean[] array, boolean check) {
    for (int i = 0; i < array.length; i++)
      if (check == array[i])
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(char[] array, char check) {
    for (int i = 0; i < array.length; i++)
      if (check == array[i])
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static <T> boolean contains(T[][] array, T check) {
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j].equals(check))
          return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(int[][] array, int check) {
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == check)
          return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   */
  public static boolean contains(double[][] array, double check) {
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == check)
          return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(boolean[][] array, boolean check) {
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == check)
          return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean contains(char[][] array, char check) {
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == check)
          return true;
    return false;
  }

  /**
   * Checks for s in the array passed.
   * 
   * @param s the String to search for
   * @param arr the array
   * @return whether s is contained or not
   */
  public static boolean contains(String s, String[] arr) {
    for (int i = 0; i < arr.length; i++)
      if (s.equals(arr[i]))
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean containsAnyOf(String s, String[] sContains) {
    for (int i = 0; i < sContains.length; i++)
      if (s.contains(sContains[i]))
        return true;
    return false;
  }

  /**
   * Checks whether duplicates occur in either array.
   * 
   * @param <T> Generic
   * @param arr1 the first array
   * @param arr2 the second array
   * @return whether arr1 contains any of the elements within arr2
   * @since 0.1.5.3.5
   */
  public static <T> boolean containsAnyOf(T[] arr1, T[] arr2) {
    HashSet<T> syn = new HashSet<>();
    syn.addAll(List.of(arr1));
    syn.addAll(List.of(arr2));
    return syn.size() != arr1.length + arr2.length;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean containsIgnoreCase(String s, String sContains) {
    return s.toLowerCase().contains(sContains.toLowerCase());
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean containsIgnoreCase(String[] array, String check) {

    for (int i = 0; i < array.length; i++)
      if (array[i].toLowerCase().equals(check.toLowerCase()))
        return true;
    return false;
  }

  /**
   * Checks for an element that is contained in the given array.
   * 
   * @param array the array to parse
   * @param check the element to check for
   * @return whether the element is contained in the array
   * @since 0.1.0.0.0
   */
  public static boolean containsIgnoreCase(String[][] array, String check) {
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j].toLowerCase().equals(check.toLowerCase()))
          return true;
    return false;
  }

  // ----------------------------COUNTING-------------------------------------

  /**
   * Counts the amount of integers that exist in a given String.
   * 
   * @param input the string to parse
   * @return the number of integers
   * @since 0.1.0.0.0
   */
  public static int countInts(String input) {
    int ints = 0;
    boolean prev = false;
    for (int i = 0; i < input.length(); i++)
      if (Character.isDigit(input.charAt(i))) {
        if (!prev) {
          ints++;
          prev = true;
        }
      } else
        prev = false;
    if (verbose)
      println("[+] " + ints + " ints found");
    return ints;
  }

  /**
   * Counts the words in a given String.
   * 
   * @param input the String to use
   * @return the number of words
   * @since 0.1.0.0.0
   */
  public static int countWords(String input) {
    int count = input.split(" ").length;
    if (verbose)
      println("[+] " + count + " words found");
    return count;
  }

  /**
   * Counts the amount of times a certain char appears in a String.
   * 
   * @param input the input to use
   * @param c the char to search for
   * @return the amount of times the given char appears in the given String
   * @since 0.1.0.0.0
   */
  public static int countChars(String input, char c) {
    int count = 0;
    for (int i = 0; i < input.length(); i++)
      if (input.charAt(i) == c)
        count++;
    if (verbose)
      println("[+] " + c + " appears in String " + count + " times");
    return count;
  }

  /**
   * Counts the number of existing lines in a String.
   * 
   * @param input the String to read
   * @return the amount of lines containing at least one character
   * @since 0.1.1.0.0
   */
  public static int countLines(String input) {
    String noEmpties = removeEmptyLines(input);
    Scanner kb = new Scanner(noEmpties);
    int count = 0;
    while (kb.hasNextLine()) {
      count++;
      kb.nextLine();
    }
    if (verbose)
      System.out.println("[+] " + count + " lines found");
    return count;
  }

  /**
   * Counts the vowels that exist in the given String.
   * 
   * @param s the String
   * @return the amount of vowels
   * @since 0.1.5.3.1
   */
  public static int countVowels(String s) {
    int count = 0;
    for (int i = 0; i < s.length(); i++)
      if (isVowel(s.charAt(i)))
        count++;
    return count;
  }

  /**
   * Counts the vowels that exist in the given String.
   * 
   * @param s the String
   * @return the amount of vowels
   * @since 0.1.5.3.1
   */
  public static int countConsonants(String s) {
    int count = 0;
    for (int i = 0; i < s.length(); i++)
      if (isConsonant(s.charAt(i)))
        count++;
    return count;
  }

  /**
   * Counts the total elements in a 2D array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to use
   * @return the amount of elements that exist in the entire array
   * @since 0.1.0.0.0
   */
  public static <T> int eCount(T[][] array) {
    int sum = 0;
    for (int i = 0; i < array.length; i++)
      sum += array[i].length;
    if (verbose)
      println("[+] " + sum + " elements found in array");
    return sum;
  }

  /**
   * Counts the total elements in a 2D array.
   * 
   * @param array the array to use
   * @return the amount of elements that exist in the entire array
   * @since 0.1.0.0.0
   */
  public static int eCount(int[][] array) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        count++;
    if (verbose)
      println("[+] " + count + " elements found in array");
    return count;
  }

  /**
   * Counts the total elements in a 2D array.
   * 
   * @param array the array to use
   * @return the amount of elements that exist in the entire array
   * @since 0.1.0.0.0
   */
  public static int eCount(double[][] array) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        count++;
    if (verbose)
      println("[+] " + count + " elements found in array");
    return count;
  }

  /**
   * Counts the total elements in a 2D array.
   * 
   * @param array the array to use
   * @return the amount of elements that exist in the entire array
   * @since 0.1.0.0.0
   */
  public static int eCount(boolean[][] array) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        count++;
    if (verbose)
      println("[+] " + count + " elements found in array");
    return count;
  }

  /**
   * Counts the total elements in a 2D array.
   * 
   * @param array the array to use
   * @return the amount of elements that exist in the entire array
   * @since 0.1.0.0.0
   */
  public static int eCount(char[][] array) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        count++;
    if (verbose)
      println("[+] " + count + " elements found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param <T> Generic
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static <T> int countFreq(T[] array, T obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      if (array[i].equals(obj))
        count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(int[] array, int obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(double[] array, double obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(boolean[] array, boolean obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(char[] array, char obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param <T> Generic
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static <T> int countFreq(T[][] array, T obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j].equals(obj))
          count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(int[][] array, int obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj)
          count++;
    if (verbose)
      println("[+]" + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(double[][] array, double obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj)
          count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(boolean[][] array, boolean obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj)
          count++;
    if (verbose)
      System.out.println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts the amount of times a specified element in an array occurs.
   * 
   * @param array the array to use
   * @param obj the element to search for
   * @return the amount of times 'obj' occurs in 'array'
   * @since 0.1.0.0.0
   */
  public static int countFreq(char[][] array, char obj) {
    int count = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj)
          count++;
    if (verbose)
      println("[+] " + count + "x " + obj + " found in array");
    return count;
  }

  /**
   * Counts how many times 'freqOf' occurs in 'toCheck'.
   * 
   * @param toCheck the String to check
   * @param freqOf the String to check frequency
   * @return the amount of times freqOf appears
   */
  public static int countFreq(String toCheck, String freqOf) {
    if (toCheck.isEmpty())
      return 0;
    return toCheck.split(freqOf, -1).length - 1;
  }

  /**
   * Creates a HashMap consisting of the frequency of every existing char.
   * 
   * @param s the String to use
   * @return the HashMap of Character frequencies
   * @since 0.1.3.2.2
   */
  public static HashMap<Character, Integer> charFreqMap(String s) {
    HashMap<Character, Integer> freqs = new HashMap<>();
    for (int i = 0; i < s.length(); i++)
      freqs.put(s.charAt(i), countChars(s, s.charAt(i)));
    if (verbose)
      println("char map built with " + freqs.size() + " entries");
    return freqs;
  }

  /**
   * Creates a HashMap consisting of the frequency of every existing String in the String passed.
   * 
   * @param s the String to use
   * @return the String frequency map
   * @since 0.1.3.2.2
   */
  public static HashMap<String, Integer> stringFreqMap(String s) {
    HashMap<String, Integer> map = new HashMap<>();
    String[] words = toStringArray(s);
    for (int i = 0; i < words.length; i++)
      map.put(words[i], countFreq(words, words[i]));
    if (verbose)
      println("[+] String map built with " + map.size() + " entries");
    return map;
  }

  // -----------------------------COLORS--------------------------------------

  /**
   * Gets an array of the AWT Colors.
   * 
   * @return on array of Colors.
   * @since 0.1.5.3.1
   */
  public static Color[] awtColorArray() {
    Color[] c = new Color[13];
    c[0] = Color.black;
    c[1] = Color.blue;
    c[2] = Color.cyan;
    c[3] = Color.darkGray;
    c[4] = Color.gray;
    c[5] = Color.green;
    c[6] = Color.lightGray;
    c[7] = Color.magenta;
    c[8] = Color.orange;
    c[9] = Color.pink;
    c[10] = Color.red;
    c[11] = Color.white;
    c[12] = Color.yellow;
    if (verbose)
      println("[+] AWT Color array created");
    return c;
  }

  /**
   * Returns the color representation of the String provided.
   * 
   * @param color the color to search for
   * @return the Color object
   * @since 0.1.5.0.0
   */
  public static Color getColor(String color) {
    Color c = null;
    if (containsIgnoreCase(color, "black"))
      c = Color.black;
    else if (containsIgnoreCase(color, "blue"))
      c = Color.blue;
    else if (containsIgnoreCase(color, "cyan"))
      c = Color.cyan;
    else if (containsIgnoreCase(color, "dark gray") || containsIgnoreCase(color, "darkgray"))
      c = Color.darkGray;
    else if (containsIgnoreCase(color, "gray"))
      c = Color.gray;
    else if (containsIgnoreCase(color, "green"))
      c = Color.green;
    else if (containsIgnoreCase(color, "light gray") || containsIgnoreCase(color, "lightgray"))
      c = Color.lightGray;
    else if (containsIgnoreCase(color, "magenta"))
      c = Color.magenta;
    else if (containsIgnoreCase(color, "orange"))
      c = Color.orange;
    else if (containsIgnoreCase(color, "pink"))
      c = Color.pink;
    else if (containsIgnoreCase(color, "red"))
      c = Color.red;
    else if (containsIgnoreCase(color, "white"))
      c = Color.white;
    else if (containsIgnoreCase(color, "yellow"))
      c = Color.yellow;
    if (verbose)
      println("[+] converted " + color + " to " + c);
    return c;
  }

  /**
   * Converts a Color to its respective String name.
   * 
   * @param color the Color
   * @return the name of the Color
   * @since 0.1.5.3.1
   */
  public static String toString(Color color) {
    String name = color.toString();
    if (color.equals(Color.black))
      name = "black";
    else if (color.equals(Color.blue))
      name = "blue";
    else if (color.equals(Color.cyan))
      name = "cyan";
    else if (color.equals(Color.darkGray))
      name = "dark gray";
    else if (color.equals(Color.gray))
      name = "gray";
    else if (color.equals(Color.green))
      name = "green";
    else if (color.equals(Color.lightGray))
      name = "light gray";
    else if (color.equals(Color.magenta))
      name = "magenta";
    else if (color.equals(Color.orange))
      name = "orange";
    else if (color.equals(Color.pink))
      name = "pink";
    else if (color.equals(Color.red))
      name = "red";
    else if (color.equals(Color.white))
      name = "white";
    else if (color.equals(Color.yellow))
      name = "yellow";
    if (verbose)
      println("[+] " + color + " converted to " + name);
    return name;
  }

  /**
   * Darkens a Color a certain amount of times.
   * 
   * @param times the amount of times to darken the Color
   * @return the new Color
   * @since 0.1.5.0.0
   */
  public static Color multiDarken(Color color, int times) {
    Color fixed = color;
    for (int i = 0; i < times; i++)
      fixed = fixed.darker();
    if (verbose)
      println("[+] darkened " + color + " to " + fixed);
    return fixed;
  }

  /**
   * Brightens a Color a certain amount of times.
   * 
   * @param times the amount of times to brighten the Color
   * @return the new Color
   * @since 0.1.5.0.0
   */
  public static Color multiBrighten(Color color, int times) {
    Color fixed = color;
    for (int i = 0; i < times; i++)
      fixed = fixed.brighter();
    if (verbose)
      println("[+] brightened " + color + " to " + fixed);
    return fixed;
  }

  /**
   * Returns an average Color for the Colors given.
   * 
   * @param colors the colors to use
   * @return an average Color
   * @since 0.1.5.0.0
   */
  public static Color mergeColors(Color... colors) {
    int[] reds = new int[colors.length];
    int[] greens = new int[colors.length];
    int[] blues = new int[colors.length];
    for (int i = 0; i < colors.length; i++) {
      reds[i] = colors[i].getRed();
      greens[i] = colors[i].getGreen();
      blues[i] = colors[i].getBlue();
    }
    if (verbose)
      println("[+] colors merged");
    return new Color((int) avg(reds), (int) avg(greens), (int) avg(blues));
  }

  /**
   * Uses the W3C algorithm to determine the brightness value for a Color.
   * 
   * @param the Color to check
   * @return the brightness value
   * @since 0.1.5.0.0
   */
  public static double brightness(Color color) {
    return Math.sqrt(0.299 * Math.pow(color.getRed(), 2) + 0.587 * Math.pow(color.getGreen(), 2)
        + 0.114 * Math.pow(color.getBlue(), 2));
  }

  /**
   * Finds the brightest Color out of the Colors passed.
   * 
   * @param colors the colors
   * @return the brightest Color
   * @since 0.1.5.3.1
   */
  public static Color brightest(Color... colors) {
    TreeMap<Double, Color> map = new TreeMap<>();
    for (int i = 0; i < colors.length; i++)
      map.put(brightness(colors[i]), colors[i]);
    if (verbose)
      println("[+] brightest color is " + toString(map.lastEntry().getValue()));
    return map.lastEntry().getValue();
  }

  /**
   * Finds the brightest Color out of the Colors passed.
   * 
   * @param colors the colors
   * @return the brightest Color
   * @since 0.1.5.3.1
   */
  public static Color darkest(Color... colors) {
    TreeMap<Double, Color> map = new TreeMap<>();
    for (int i = 0; i < colors.length; i++)
      map.put(brightness(colors[i]), colors[i]);
    if (verbose)
      println("[+] darkest color is " + toString(map.firstEntry().getValue()));
    return map.firstEntry().getValue();
  }

  // ----------------------------CREATION-------------------------------------

  /**
   * Generates a String that includes the char passed count amount of times.
   * 
   * @param c the character to repeat
   * @param count the amount of times the char may appear
   * @return the repeated characters
   * @since 0.1.5.3.3
   */
  public static String generate(char c, int count) {
    StringBuilder s = new StringBuilder("");
    IntStream.range(0, count).forEach(i -> s.append(c));
    return s.toString();
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static String[] createStringArray(int length) {
    String[] array = new String[length];
    for (int i = 0; i < array.length; i++)
      array[i] = randomWord();
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static int[] createIntArray(int length) {
    int[] array = new int[length];
    for (int i = 0; i < array.length; i++)
      array[i] = randomInt(0, 10);
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static double[] createDoubleArray(int length) {
    double[] array = new double[length];
    for (int i = 0; i < array.length; i++)
      array[i] = Msn.decFormat(Msn.random(0, 10), 2);
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static char[] createCharArray(int length) {
    char[] array = new char[length];
    for (int i = 0; i < array.length; i++)
      array[i] = randomLetter();
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static boolean[] createBoolArray(int length) {
    boolean[] array = new boolean[length];
    for (int i = 0; i < array.length; i++)
      array[i] = coinflip();
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static String[][] create2DStringArray(int rows, int cols) {
    String[][] array = new String[rows][cols];
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        array[i][j] = Msn.randomWord();
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static int[][] create2DIntArray(int rows, int cols) {
    int[][] array = new int[rows][cols];
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        array[i][j] = randomInt(0, 10);
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static double[][] create2DDoubleArray(int rows, int cols) {
    double[][] array = new double[rows][cols];
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        array[i][j] = Msn.decFormat(Msn.random(0, 10), 2);
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static char[][] create2DCharArray(int rows, int cols) {
    char[][] array = new char[rows][cols];
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        array[i][j] = randomLetter();
    return array;
  }

  /**
   * Generates an array with random values.
   * 
   * @param length the length of the array
   * @return the new array
   * @since 0.1.5.1.0
   */
  public static boolean[][] create2DBoolArray(int rows, int cols) {
    boolean[][] array = new boolean[rows][cols];
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        array[i][j] = coinflip();
    return array;
  }

  /**
   * Creates a 2D array from the Object array's dimensions.
   * 
   * @param array the array
   * @return the new int[][]
   * @since 0.1.5.2.5
   */
  public static int[][] create2DIntArrayFromObj(Object[][] array) {
    int[][] destination = new int[array.length][];
    for (int i = 0; i < destination.length; i++) {
      destination[i] = new int[array[i].length];
      for (int j = 0; j < destination[i].length; j++)
        destination[i][j] = 0;
    }
    return destination;
  }

  /**
   * Creates a 2D array from the Object array's dimensions.
   * 
   * @param array the array
   * @return the new int[][]
   * @since 0.1.5.2.5
   */
  public static double[][] create2DDoubleArrayFromObj(Object[][] array) {
    double[][] destination = new double[array.length][];
    for (int i = 0; i < destination.length; i++) {
      destination[i] = new double[array[i].length];
      for (int j = 0; j < destination[i].length; j++)
        destination[i][j] = 0;
    }
    return destination;
  }

  // ----------------------------STRINGS--------------------------------------

  /**
   * Boxes a String.
   * 
   * @param s the String
   * @return the boxed String
   */
  public static String boxed(String s) {
    String b = "";
    String[] lines = Msn.toLineArray(s);
    int max = lines[0].length();
    for (int i = 0; i < lines.length; i++)
      if (lines[i].length() > max)
        max = lines[i].length();
    String seq = generate('-', max + 2);
    b += "+" + seq + "+\n";
    for (String l : lines) {
      b += "| " + l;
      b += generate(' ', max - l.length() + 1) + "|\n";
    }
    b += "+" + seq + "+\n";
    return b;
  }

  /**
   * Verifies that every character in the passed String is an English character.
   * 
   * @param s the String to parse
   * @return whether the String is entirely comprised of English chars or not
   * @since 0.1.0.0.3
   */
  public static boolean isEnglish(String s) {
    for (int i = 0; i < s.length(); i++)
      if (!contains(alphabetArray(), s.charAt(i)))
        return false;
    return true;
  }

  /**
   * Determines whether every char passed in the String is a digit.
   * 
   * @param s the String
   * @return whether every char passed in the String is a digit
   */
  public static boolean isNumber(String s) {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return true;
  }

  /**
   * Reverses the character order of the given String.
   * 
   * @param s the String to use
   * @return the new String
   * @since 0.1.2.2.5
   */
  public static String reverse(String s) {
    if (verbose)
      println("[*] reversing String");
    return new StringBuilder(s).reverse().toString();
  }

  /**
   * Counts the most common character in the String.
   * 
   * @param s the String to use
   * @return the most common character
   * @since 0.1.1.0.4
   */
  public static char mostCommonChar(String s) {
    TreeMap<Integer, Character> freq = new TreeMap<>();
    for (int i = 0; i < s.length(); i++)
      freq.put(countChars(s, s.charAt(i)), s.charAt(i));
    return freq.lastEntry().getValue();
  }

  /**
   * Extracts all words from the given String.
   * 
   * @param s the String
   * @return the words existing in the String
   */
  public static String[] getWords(String s) {
    if (!s.contains(" ")) {
      return new String[] {s};
    }
    return s.split(" ");
  }

  /**
   * Returns a certain line in the String given.
   * 
   * @param s the String to use
   * @param index the index of the specified line
   * @return the line specified
   * @since 0.1.1.5.0
   */
  public static String getLine(String s, int index) {
    int lines = countLines(s);
    if (index > lines)
      throw new IllegalArgumentException(
          "index " + index + " out of bounds for line count " + lines);
    Scanner kb = new Scanner(s);
    for (int i = 0; i < index; i++)
      kb.nextLine();
    return kb.nextLine();
  }

  /**
   * Converts a String into an array of lines existing in that String.
   * 
   * @param s the String to use
   * @return array value of each line in the String given
   * @since 0.1.1.5.0
   */
  public static String[] toLineArray(String s) {
    Scanner kb = new Scanner(s);
    String[] lineArray = new String[countLines(s)];
    for (int i = 0; i < lineArray.length; i++)
      if (kb.hasNextLine())
        lineArray[i] = kb.nextLine();
      else
        lineArray[i] = "";
    if (verbose)
      println("[+] line array created");
    return lineArray;
  }

  /**
   * Removes the specified lines in a String with line index format.
   * 
   * @param s the String to use
   * @param startLine the first line (inclusive)
   * @param finishLine the line to stop deletion (inclusive)
   * @return the fixed String
   * @since 0.1.1.5.0
   */
  public static String removeLines(String s, int startLine, int finishLine) {
    String fixed = "";
    String[] lines = toLineArray(s);
    for (int i = 0; i < lines.length; i++)
      if (i < startLine || i > finishLine)
        fixed += lines[i] + "\n";
    if (verbose)
      println("[+] lines removed");
    return fixed;
  }

  /**
   * Gets the section of lines between startLine and finishLine, inclusively.
   * 
   * @param s the String to use
   * @param startLine the starting point
   * @param finishLine the ending point
   * @return the lines including and between the lines specified
   * @since 0.1.1.5.0
   */
  public static String getLines(String s, int startLine, int finishLine) {
    String fixed = "";
    String[] lines = toLineArray(s);
    for (int i = 0; i < lines.length; i++)
      if (i >= startLine && i <= finishLine)
        fixed += lines[i] + "\n";
    if (verbose)
      println("[+] lines " + startLine + " through " + finishLine + " found");
    return fixed;
  }

  /**
   * Inserts a newline escape sequence at a certain index, useful for formatting longer Strings.
   * 
   * @param s the String to use
   * @param index the common index to insert the escape sequence
   * @return the new String
   */
  public static String formatString(String s, int index) {
    StringBuilder sb = new StringBuilder(s);
    int index2 = index;
    int offset = 0;
    try {
      while (true) {
        while (sb.charAt(index2) != ' ') {
          index2--;
          offset++;
        }
        sb.replace(index2, index2 + 1, "\n");
        index2 = index2 + offset + index;
      }
    } catch (StringIndexOutOfBoundsException e) {
      System.out.print("");
    }
    if (verbose)
      System.out.println("String successfully formatted");
    return sb.toString();
  }

  /**
   * Formats a double to the specified amount of decimal places.
   * 
   * @param toFormat the number to format
   * @param decPlaces the number of decimal places to format to
   * @return the formatted double with proper decimal places
   * @since 0.1.3.0.4
   */
  public static double decFormat(double toFormat, int decPlaces) {
    return Double.valueOf(String.format("%." + String.valueOf(decPlaces) + "f", toFormat));
  }

  /**
   * Formats a number with commas and two decimal places.
   * 
   * @param toFormat the number to format
   * @return the formatted number as a String 0.1.3.0.4
   */
  public static String formatNumber(double toFormat) {
    return String.format("%,.2f", toFormat);
  }

  /**
   * Formats a number with commas and 2 decimal places. Also adds a $ in front of the number,
   * however if the number is negative it is formatted as -$.
   * 
   * EX: -52342.6236 would be formatted as -$52,342.62 EX: 68734.66092 would be formatted as
   * $68,734.66
   * 
   * @param toFormat the number to format
   * @return the formatted number in String form 0.1.3.0.4
   */
  public static String moneyFormat(double toFormat) {
    StringBuilder sb = new StringBuilder(String.format("%,.2f", toFormat).replace("-", ""));
    if (toFormat < 0) {
      sb.insert(0, "-$");
      return sb.toString();
    }
    return "$" + sb.toString();
  }

  /**
   * Rebuilds a String based on data types.
   * 
   * @param s the String to use
   * @return the sorted String
   * @since 0.1.2.0.5
   */
  public static String sortString(String s) {
    char[] chars = s.toCharArray();
    Arrays.sort(chars);
    return String.valueOf(chars);
  }

  /**
   * Parses through a String, removing all letters.
   * 
   * @param s the String to parse.
   * @return the String without numbers
   */
  public static int[] extractNums(String s) {
    ArrayList<Integer> ints = new ArrayList<>();
    for (int i = 0; i < s.length(); i++)
      if (Character.isDigit(s.charAt(i)))
        ints.add(Integer.valueOf(s.charAt(i)));
    return toInt(ints.toArray());
  }

  /**
   * Performs toUpperCase() on every word in the array.
   * 
   * @param array the array to capitalize
   * @return the fixed array
   * @since 0.1.5.3.1
   */
  public static void toUpperCase(String[] array) {
    for (int i = 0; i < array.length; i++)
      array[i] = array[i].toUpperCase();
  }

  /**
   * Performs toLowerCase() on every word in the array.
   * 
   * @param array the array to make lower case
   * @return the fixed array
   * @since 0.1.5.3.1
   */
  public static void toLowerCase(String[] array) {
    for (int i = 0; i < array.length; i++)
      array[i] = array[i].toLowerCase();
  }

  /**
   * Capitalizes the first letter of the String passed.
   * 
   * @param s the String
   * @return the fixed String
   * @since 0.1.5.3.1
   */
  public static String capitalize(String s) {
    String fixed = "";
    for (int i = 0; i < s.length(); i++)
      if (i == 0)
        fixed += String.valueOf(s.charAt(i)).toUpperCase();
      else
        fixed += s.charAt(i);
    return fixed;
  }

  /**
   * Capitalizes every word in the array passed.
   * 
   * @param array the fixed array
   * @since 0.1.5.3.1
   */
  public static void capitalize(String[] array) {
    for (int i = 0; i < array.length; i++)
      array[i] = capitalize(array[i]);
  }

  /**
   * Removes the last character of the String passed.
   * 
   * @param s the String
   * @return the fixed String
   * @since 0.1.5.3.2
   */
  public static String backspace(String s) {
    String news = "";
    for (int i = 0; i < s.length() - 1; i++)
      news += s.charAt(i);
    return news;
  }

  /**
   * Takes a char array and turns it into a String array.
   * 
   * @param array to array to modify
   * @return the String array
   */
  public static String[] toStringArray(char[] array) {
    String[] s = new String[array.length];
    for (int i = 0; i < array.length; i++)
      s[i] = Character.toString(array[i]);
    if (verbose)
      println("[+] String array created");
    return s;
  }

  /**
   * Takes a char array and turns it into a String array.
   * 
   * @param array to array to modify
   * @return the String array
   */
  public static String[][] toStringArray(char[][] array) {
    if (isRagged(array))
      throw new IllegalArgumentException("Char 2D array must not be ragged");
    String[][] s = new String[array.length][array[0].length];
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        s[i][j] = Character.toString(array[i][j]);
    if (verbose)
      println("[+] String array created");
    return s;
  }

  /**
   * Converts the given String into an array of words.
   * 
   * @param s the String to use
   * @return the String array of words
   */
  public static String[] toStringArray(String s) {
    if (verbose)
      println("[*] creating String array");
    return s.split(" ");
  }

  /**
   * Gets all existing numbers in the String, including doubles and ints
   * 
   * @param s the String to analyze
   * @return the numbers
   * @since 0.1.5.3.2
   */
  public static double[] extractNumbers(String s) {
    String fixed = "";
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '.' || Character.isDigit(s.charAt(i))) {
        fixed += s.charAt(i);
      } else {
        fixed += " ";
      }
    }
    System.out.println(fixed);
    Scanner k = new Scanner(fixed);
    ArrayList<Double> numbers = new ArrayList<>();
    while (k.hasNext()) {
      try {
        numbers.add(k.nextDouble());
      } catch (Exception e) {
        numbers.add((double) k.nextInt());
      }
    }
    return toDouble(numbers.toArray());
  }

  /**
   * Converts an array into a single String of every element in the array.
   * 
   * @param array the array to use
   * @return the String representation of every element in the array
   */
  public static String toSequence(String[] array) {
    String s = "";
    for (int i = 0; i < array.length; i++) {
      s += array[i];
      if (i != array.length - 1)
        s += " ";
    }
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts an array into a single String of every element in the array.
   * 
   * @param array the array to use
   * @return the String representation of every element in the array
   */
  public static String toSequence(int[] array) {
    String s = "";
    for (int i = 0; i < array.length; i++) {
      s += array[i];
      if (i != array.length - 1)
        s += " ";
    }
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts an array into a single String of every element in the array.
   * 
   * @param array the array to use
   * @return the String representation of every element in the array
   */
  public static String toSequence(double[] array) {
    String s = "";
    for (int i = 0; i < array.length; i++) {
      s += array[i];
      if (i != array.length - 1)
        s += " ";
    }
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts an array into a single String of every element in the array.
   * 
   * @param array the array to use
   * @return the String representation of every element in the array
   */
  public static String toSequence(boolean[] array) {
    String s = "";
    for (int i = 0; i < array.length; i++) {
      s += array[i];
      if (i != array.length - 1)
        s += " ";
    }
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts an array into a single String of every element in the array.
   * 
   * @param array the array to use
   * @return the String representation of every element in the array
   */
  public static String toSequence(char[] array) {
    String s = "";
    for (int i = 0; i < array.length; i++) {
      s += array[i];
    }
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts an Object array to a String array.
   * 
   * @param array the array to use
   * @return the String[]
   */
  public static String[] toString(Object[] array) {
    if (verbose)
      println("[*] converting Object[] to String[]");
    return Arrays.copyOf(array, array.length, String[].class);
  }

  /**
   * Converts a 2D array into a single String comprising of every element that exists in the array.
   * 
   * @param array the array to use
   * @return the String representation of the array
   */
  public static String toSequence(String[][] array) {
    String s = "";
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        s += array[i][j];
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts a 2D array into a single String comprising of every element that exists in the array.
   * 
   * @param array the array to use
   * @return the String representation of the array
   */
  public static String toSequence(int[][] array) {
    String s = "";
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        s += array[i][j];
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts a 2D array into a single String comprising of every element that exists in the array.
   * 
   * @param array the array to use
   * @return the String representation of the array
   */
  public static String toSequence(double[][] array) {
    String s = "";
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        s += array[i][j] + " ";
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts a 2D array into a single String comprising of every element that exists in the array.
   * 
   * @param array the array to use
   * @return the String representation of the array
   */
  public static String toSequence(boolean[][] array) {
    String s = "";
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        s += array[i][j] + " ";
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  /**
   * Converts a 2D array into a single String comprising of every element that exists in the array.
   * 
   * @param array the array to use
   * @return the String representation of the array
   */
  public static String toSequence(char[][] array) {
    String s = "";
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        s += array[i][j];
    if (verbose)
      println("[+] Sequence created: " + s);
    return s;
  }

  // -------------------------TEXT FILE OPERATIONS--------------------------

  /**
   * Reads a file line-by-line. Can be used to remove empty lines with replaceAll("(?m)^\\s", "") or
   * with removeEmptyLines().
   * 
   * https://howtodoinjava.com/
   * 
   * @param path the file path
   * @return the contents of the file specified
   * @since 0.1.2.1.0
   */
  public static String contentsOf(String filePath) {
    StringBuilder contentBuilder = new StringBuilder();
    try (Stream<String> STREAM = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
      STREAM.forEach(s -> contentBuilder.append(s).append("\n"));
    } catch (IOException e) {
      System.out.println("file not found");
    }
    return contentBuilder.toString();
  }

  /**
   * Removes all empty lines in a given String.
   * 
   * @param s the String to use
   * @return the String representation with no empty lines
   * @since 0.1.2.1.0
   */
  public static String removeEmptyLines(String s) {
    return s.replaceAll("(?m)^\\s", "");
  }

  /**
   * Removes all elements that are equal to "" in the array passed.
   * 
   * @param array the array
   * @return the fixed String
   */
  public static String[] removeEmptyStrings(String[] array) {
    ArrayList<String> rem = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (!array[i].equals(""))
        rem.add(array[i]);
    return rem.toArray(String[]::new);
  }

  /**
   * Returns the contents of a file without empty lines.
   * 
   * @param path the path of the file
   * @return the string representation without empty lines
   * @since 0.1.2.1.0
   */
  public static String contentsOfNoEmptyLines(String path) {
    StringBuilder contentBuilder = new StringBuilder();
    try (Stream<String> STREAM = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
      STREAM.forEach(s -> contentBuilder.append(s).append("\n"));
    } catch (IOException e) {
      System.out.print("file not found");
    }
    return contentBuilder.toString().replaceAll("(?m)^\\s", "");
  }

  /**
   * Prepares a file to be parsed and returns the Scanner containing the contents of the file.
   * 
   * @param path the path to the file to read
   * @return the Scanner that will be used to parse through the file
   * @since 0.1.2.1.0
   */
  public static Scanner prepare(String path) {
    Scanner s = new Scanner(contentsOfNoEmptyLines(path));
    if (verbose)
      println("[+] scanner prepared");
    return s;
  }

  /**
   * Replaces the most common character in the File given with a whitespace.
   * 
   * @param path the path to the file
   * @return the String version of the file with spaces
   * @throws FileNotFoundException
   * @since 0.1.2.1.0
   */
  public static String toSSV(String path) {
    String contents = contentsOf(path).toLowerCase();
    return contents.replaceAll(String.valueOf(mostCommonChar(contents)), " ");
  }

  /**
   * Writes specified text into a new txt file (given path).
   * 
   * @param path the path to the file
   * @param text the text
   * @throws FileNotFoundException cannot find file
   * @since 0.1.2.1.0
   */
  public static void writeTo(String path, String text) throws FileNotFoundException {
    PrintWriter pw = new PrintWriter(new File(path));
    pw.write(text);
    pw.close();
  }

  /**
   * Prompts a message to the console.
   * 
   * @param msg the message to be read
   * @return the String read
   */
  public static Object prompt(String msg, Scanner sc) {
    p(msg);
    Object ret = null;
    boolean found = false;
    try {
      ret = sc.nextInt();
      found = true;
    } catch (Exception e) {
    }
    try {
      if (!found)
        ret = sc.nextDouble();
      found = true;
    } catch (Exception e) {
    }
    try {
      if (!found)
        ret = sc.next();
      found = true;
    } catch (Exception e) {
    }
    return ret;
  }

  // ---------------------------ARRAY OPERATIONS------------------------------

  /**
   * Prints an array, useful when needing to print arrays quickly.
   * 
   * @param <T> Generic
   * 
   * @param arr the array to print
   * @since 0.1.0.0.0
   */
  public static <T> void pa(T[] arr) {
    System.out.println(Arrays.toString(arr));
  }

  /**
   * Prints an array, useful when needing to print arrays quickly.
   * 
   * @param arr the array to print
   * @since 0.1.0.0.0
   */
  public static void pa(int[] arr) {
    System.out.println(Arrays.toString(arr));
  }

  /**
   * Prints an array, useful when needing to print arrays quickly.
   * 
   * @param arr the array to print
   * @since 0.1.0.0.0
   */
  public static void pa(double[] arr) {
    System.out.println(Arrays.toString(arr));
  }

  /**
   * Prints an array, useful when needing to print arrays quickly.
   * 
   * @param arr the array to print
   * @since 0.1.0.0.0
   */
  public static void pa(boolean[] arr) {
    System.out.println(Arrays.toString(arr));
  }

  /**
   * Prints an array, useful when needing to print arrays quickly.
   * 
   * @param arr the array to print
   * @since 0.1.0.0.0
   */
  public static void pa(char[] arr) {
    System.out.println(Arrays.toString(arr));
  }

  /**
   * Prints a 2D array thats a little easier on the eyes.
   * 
   * @param <T> Generic
   * 
   * @param matrix the 2D matrix to print
   * @since 0.1.0.0.0
   */
  public static <T> void pa(T[][] matrix) {
    println(
        Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
  }

  /**
   * Prints a 2D array thats a little easier on the eyes.
   * 
   * @param matrix the 2D matrix to print
   * @since 0.1.0.0.0
   */
  public static void pa(String[][] matrix) {
    println(
        Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
  }

  /**
   * Prints a 2D array thats a little easier on the eyes.
   * 
   * @param matrix the 2D matrix to print
   * @since 0.1.0.0.0
   */
  public static void pa(int[][] matrix) {
    println(
        Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
  }

  /**
   * Prints a 2D array thats a little easier on the eyes.
   * 
   * @param matrix the 2D matrix to print
   * @since 0.1.0.0.0
   */
  public static void pa(double[][] matrix) {
    println(
        Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
  }

  /**
   * Prints a 2D array thats a little easier on the eyes.
   * 
   * @param matrix the 2D matrix to print
   * @since 0.1.0.0.0
   */
  public static void pa(boolean[][] matrix) {
    println(
        Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
  }

  /**
   * Prints a 2D array thats a little easier on the eyes.
   * 
   * @param matrix the 2D matrix to print
   * @since 0.1.0.0.0
   */
  public static void pa(char[][] matrix) {
    println(
        Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
  }

  /**
   * Reverses the passed array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to reverse
   * @since 0.1.1.0.0
   */
  public static Object[] reverse(Object[] array) {
    int index = 0;
    Object[] copy = new Object[array.length];
    for (int i = array.length - 1; i >= 0; i--) {
      copy[index] = array[i];
      index++;
    }
    if (verbose)
      println("[+] array reversed");
    return copy;
  }

  /**
   * Reverses the passed array.
   * 
   * @param array the array to reverse
   * @since 0.1.1.0.0
   */
  public static int[] reverse(int[] array) {
    int index = 0;
    int[] copy = new int[array.length];
    for (int i = array.length - 1; i >= 0; i--) {
      copy[index] = array[i];
      index++;
    }
    if (verbose)
      println("[+] array reversed");
    return copy;
  }

  /**
   * Reverses the passed array.
   * 
   * @param array the array to reverse
   * @since 0.1.1.0.0
   */
  public static double[] reverse(double[] array) {
    int index = 0;
    double[] copy = new double[array.length];
    for (int i = array.length - 1; i >= 0; i--) {
      copy[index] = array[i];
      index++;
    }
    if (verbose)
      println("[+] array reversed");
    return copy;
  }

  /**
   * Reverses the passed array.
   * 
   * @param array the array to reverse
   * @since 0.1.1.0.0
   */
  public static char[] reverse(char[] array) {
    int index = 0;
    char[] copy = new char[array.length];
    for (int i = array.length - 1; i >= 0; i--) {
      copy[index] = array[i];
      index++;
    }
    if (verbose)
      println("[+] array reversed");
    return copy;
  }

  /**
   * Sorts an array, then reverses it.
   * 
   * @param <T> Generic
   * 
   * @param array the array to sort and reverse
   * @return the fixed array
   * @since 0.1.5.3.2
   */
  public static Object[] reverseSort(Object[] array) {
    Arrays.sort(array);
    return reverse(array);
  }

  /**
   * Sorts an array, then reverses it.
   * 
   * @param array the array to sort and reverse
   * @return the fixed array
   * @since 0.1.5.3.2
   */
  public static int[] reverseSort(int[] array) {
    Arrays.sort(array);
    return reverse(array);
  }

  /**
   * Sorts an array, then reverses it.
   * 
   * @param array the array to sort and reverse
   * @return the fixed array
   * @since 0.1.5.3.2
   */
  public static double[] reverseSort(double[] array) {
    Arrays.sort(array);
    return reverse(array);
  }

  /**
   * Sorts an array, then reverses it.
   * 
   * @param array the array to sort and reverse
   * @return the fixed array
   * @since 0.1.5.3.2
   */
  public static char[] reverseSort(char[] array) {
    Arrays.sort(array);
    return reverse(array);
  }

  /**
   * Checks whether the coordinates are valid.
   * 
   * @param <T> Generic
   * 
   * @param array the array to check
   * @param coord the coordinates
   * @return whether the coordinates passed are valid
   * @since 0.1.5.0.4
   */
  public static <T> boolean validCoord(T[][] array, int[] coord) {
    try {
      Object e = array[coord[0]][coord[1]];
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the coordinates are valid.
   * 
   * @param <E> e
   * @param array the array to check
   * @param coord the coordinates
   * @return whether the coordinates passed are valid
   * @since 0.1.5.0.4
   */
  public static boolean validCoord(int[][] array, int[] coord) {
    try {
      Object e = array[coord[0]][coord[1]];
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the coordinates are valid.
   * 
   * @param <E> e
   * @param array the array to check
   * @param coord the coordinates
   * @return whether the coordinates passed are valid
   * @since 0.1.5.0.4
   */
  public static boolean validCoord(double[][] array, int[] coord) {
    try {
      Object e = array[coord[0]][coord[1]];
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the coordinates are valid.
   * 
   * @param <E> e
   * @param array the array to check
   * @param coord the coordinates
   * @return whether the coordinates passed are valid
   * @since 0.1.5.0.4
   */
  public static boolean validCoord(boolean[][] array, int[] coord) {
    try {
      Object e = array[coord[0]][coord[1]];
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the coordinates are valid.
   * 
   * @param <E> e
   * @param array the array to check
   * @param coord the coordinates
   * @return whether the coordinates passed are valid
   * @since 0.1.5.0.4
   */
  public static boolean validCoord(char[][] array, int[] coord) {
    try {
      Object e = array[coord[0]][coord[1]];
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Copies an array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to copy
   * @return the new array
   * @since 0.1.5.0.4
   */
  public static <T> T[] arraycopy(T[] array) {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Copies an array.
   * 
   * @param array the array to copy
   * @return the new array
   * @since 0.1.5.0.4
   */
  public static int[] arraycopy(int[] array) {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Copies an array.
   * 
   * @param array the array to copy
   * @return the new array
   * @since 0.1.5.0.4
   */
  public static double[] arraycopy(double[] array) {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Copies an array.
   * 
   * @param array the array to copy
   * @return the new array
   * @since 0.1.5.0.4
   */
  public static char[] arraycopy(char[] array) {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Copies an array.
   * 
   * @param array the array to copy
   * @return the new array
   * @since 0.1.5.0.4
   */
  public static boolean[] arraycopy(boolean[] array) {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Provides a new 2D array with the same dimensions. ALLOWS FOR RAGGED ARRAYS.
   * 
   * @param <T> Generic
   * 
   * @param array the array
   * @return a new array
   * @since 0.1.5.0.4
   */
  public static Object[][] arraycopy(Object[][] array) {
    Object[][] newarr = new Object[array.length][];
    for (int i = 0; i < array.length; i++) {
      Object[] aMatrix = array[i];
      newarr[i] = new Object[aMatrix.length];
      System.arraycopy(aMatrix, 0, newarr[i], 0, aMatrix.length);
    }
    if (verbose)
      println("[+] array copied");
    return newarr;
  }

  /**
   * Provides a new 2D array with the same dimensions. ALLOWS FOR RAGGED ARRAYS.
   * 
   * @param <E> e
   * @param array the array
   * @return a new array
   * @since 0.1.5.0.4
   */
  public static int[][] arraycopy(int[][] array) {
    int[][] newarr = new int[array.length][];
    for (int i = 0; i < array.length; i++) {
      int[] aMatrix = array[i];
      newarr[i] = new int[aMatrix.length];
      System.arraycopy(aMatrix, 0, newarr[i], 0, aMatrix.length);
    }
    if (verbose)
      println("[+] array copied");
    return newarr;
  }

  /**
   * Provides a new 2D array with the same dimensions. ALLOWS FOR RAGGED ARRAYS.
   * 
   * @param <E> e
   * @param array the array
   * @return a new array
   * @since 0.1.5.0.4
   */
  public static double[][] arraycopy(double[][] array) {
    double[][] newarr = new double[array.length][];
    for (int i = 0; i < array.length; i++) {
      double[] aMatrix = array[i];
      newarr[i] = new double[aMatrix.length];
      System.arraycopy(aMatrix, 0, newarr[i], 0, aMatrix.length);
    }
    if (verbose)
      println("[+] array copied");
    return newarr;
  }

  /**
   * Provides a new 2D array with the same dimensions. ALLOWS FOR RAGGED ARRAYS.
   * 
   * @param <E> e
   * @param array the array
   * @return a new array
   * @since 0.1.5.0.4
   */
  public static boolean[][] arraycopy(boolean[][] array) {
    boolean[][] newarr = new boolean[array.length][];
    for (int i = 0; i < array.length; i++) {
      boolean[] aMatrix = array[i];
      newarr[i] = new boolean[aMatrix.length];
      System.arraycopy(aMatrix, 0, newarr[i], 0, aMatrix.length);
    }
    if (verbose)
      println("[+] array copied");
    return newarr;
  }

  /**
   * Provides a new 2D array with the same dimensions. ALLOWS FOR RAGGED ARRAYS.
   * 
   * @param <E> e
   * @param array the array
   * @return a new array
   * @since 0.1.5.0.4
   */
  public static char[][] arraycopy(char[][] array) {
    char[][] newarr = new char[array.length][];
    for (int i = 0; i < array.length; i++) {
      char[] aMatrix = array[i];
      newarr[i] = new char[aMatrix.length];
      System.arraycopy(aMatrix, 0, newarr[i], 0, aMatrix.length);
    }
    if (verbose)
      println("[+] array copied");
    return newarr;
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param <T> Generic
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static <T> void importAll(T[] from, Collection<T> to) {
    for (T o : from)
      to.add(o);
    if (verbose)
      println("[+] values from imported into Collection");
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(int[] from, Collection<Integer> to) {
    for (Integer o : from)
      to.add((Integer) o);
    if (verbose)
      println("[+] values from imported into Collection");
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(double[] from, Collection<Double> to) {
    for (Double o : from)
      to.add((Double) o);
    if (verbose)
      println("[+] values from imported into Collection");
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(char[] from, Collection<Character> to) {
    for (Character o : from)
      to.add((Character) o);
    if (verbose)
      println("[+] values from imported into Collection");
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(boolean[] from, Collection<Boolean> to) {
    for (Boolean o : from)
      to.add((Boolean) o);
    if (verbose)
      println("[+] values from imported into Collection");
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param <T> Generic
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static <T> void importAll(T[][] from, Collection<T> to) {
    for (int i = 0; i < from.length; i++)
      for (int j = 0; j < from[i].length; j++)
        to.add(from[i][j]);
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(int[][] from, Collection<Integer> to) {
    for (int i = 0; i < from.length; i++)
      for (int j = 0; j < from[i].length; j++)
        to.add(from[i][j]);
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(double[][] from, Collection<Double> to) {
    for (int i = 0; i < from.length; i++)
      for (int j = 0; j < from[i].length; j++)
        to.add(from[i][j]);
  }

  /**
   * Imports all values from the array into the Collection specified.
   * 
   * @param from the from array
   * @param to the collection to import the arrays values to
   * @since 0.1.4.3.2
   */
  public static void importAll(char[][] from, Collection<Character> to) {
    for (int i = 0; i < from.length; i++)
      for (int j = 0; j < from[i].length; j++)
        to.add(from[i][j]);
  }

  /**
   * Removes all instances of the specified element in an array.
   * 
   * @param array the array to use
   * @param toRemove the element to remove
   * @return the fixed array
   * @since 0.1.4.3.2
   */
  public static Object[] removeAll(Object[] array, Object toRemove) {
    ArrayList<Object> wo = new ArrayList<>(Arrays.asList(array));
    while (wo.contains(toRemove))
      wo.remove(toRemove);
    if (verbose)
      System.out.println("[+] removed " + toRemove + " from array");
    return wo.toArray();
  }

  /**
   * Removes all instances of the specified element in an array.
   * 
   * @param array the array to use
   * @param toRemove the element to remove
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static int[] removeAll(int[] array, int toRemove) {
    ArrayList<Integer> fixed = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] != toRemove)
        fixed.add(array[i]);
    if (verbose)
      println("[+] removed " + toRemove + " from array");
    return toInt(fixed.toArray());
  }

  /**
   * Removes all instances of the specified element in an array.
   * 
   * @param array the array to use
   * @param toRemove the element to remove
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static double[] removeAll(double[] array, double toRemove) {
    ArrayList<Double> fixed = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] != toRemove)
        fixed.add(array[i]);
    if (verbose)
      println("[+] removed " + toRemove + " from array");
    return toDouble(fixed.toArray());
  }

  /**
   * Removes all instances of the specified element in an array.
   * 
   * @param array the array to use
   * @param toRemove the element to remove
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static char[] removeAll(char[] array, char toRemove) {
    ArrayList<Character> fixed = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] != toRemove)
        fixed.add(array[i]);
    if (verbose)
      println("[+] removed " + toRemove + " from array");
    return toChar(fixed.toArray());
  }

  /**
   * Removes all of the given values in toRemove from array.
   * 
   * @param array the array to remove elements from
   * @param toRemove the elements to remove from array
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static Object[] removeAll(Object[] array, Object[] toRemove) {
    Object[] a = array;
    for (int i = 0; i < toRemove.length; i++)
      a = removeAll(a, toRemove[i]);
    return a;
  }

  /**
   * Removes all of the given values in toRemove from array.
   * 
   * @param array the array to remove elements from
   * @param toRemove the elements to remove from array
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static int[] removeAll(int[] array, int[] toRemove) {
    int[] a = array;
    for (int i = 0; i < toRemove.length; i++)
      a = removeAll(a, toRemove[i]);
    return a;
  }

  /**
   * Removes all of the given values in toRemove from array.
   * 
   * @param array the array to remove elements from
   * @param toRemove the elements to remove from array
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static double[] removeAll(double[] array, double[] toRemove) {
    double[] a = array;
    for (int i = 0; i < toRemove.length; i++)
      a = removeAll(a, toRemove[i]);
    return a;
  }

  /**
   * Removes all of the given values in toRemove from array.
   * 
   * @param array the array to remove elements from
   * @param toRemove the elements to remove from array
   * @return the fixed array
   * @since 0.1.2.4.0
   */
  public static char[] removeAll(char[] array, char[] toRemove) {
    char[] a = array;
    for (int i = 0; i < toRemove.length; i++)
      a = removeAll(a, toRemove[i]);
    return a;
  }

  /**
   * Returns a vertical piece of the passed 2D array in the form of a 1D array.
   * 
   * ex. a 2D array such as [5, 3, 8] [4, 0, 3] [2, 3, 3] when running parseVertArray(array, 1)
   * would return {3, 0, 3}
   * 
   * @param array the array to use
   * @param index the row index to parse
   * @return the parsed array
   * @since 0.1.0.1.0
   */
  public static Object[] parseVertArray(Object[][] array, int index) {
    if (isRagged(array))
      throw new IllegalArgumentException("array cannot be ragged");
    Object[] vert = new Object[array[0].length];
    for (int i = 0; i < vert.length; i++)
      vert[i] = array[i][index];
    if (verbose)
      println("[+] parsed " + Arrays.toString(vert));
    return vert;
  }

  /**
   * Returns a vertical piece of the passed 2D array in the form of a 1D array.
   * 
   * ex. a 2D array such as [5, 3, 8] [4, 0, 3] [2, 3, 3] when running parseVertArray(array, 1)
   * would return {3, 0, 3}
   * 
   * @param array the array to use
   * @param index the row index to parse
   * @return the parsed array
   * @since 0.1.0.1.0
   */
  public static int[] parseVertArray(int[][] array, int index) {
    if (isRagged(array))
      throw new IllegalArgumentException("array cannot be ragged");
    int[] vert = new int[array[0].length];
    for (int i = 0; i < vert.length; i++)
      vert[i] = array[i][index];
    if (verbose)
      println("[+] parsed " + Arrays.toString(vert));
    return vert;
  }

  /**
   * Returns a vertical piece of the passed 2D array in the form of a 1D array.
   * 
   * ex. a 2D array such as [5, 3, 8] [4, 0, 3] [2, 3, 3] when running parseVertArray(array, 1)
   * would return {3, 0, 3}
   * 
   * @param array the array to use
   * @param index the row index to parse
   * @return the parsed array
   * @since 0.1.0.1.0
   */
  public static double[] parseVertArray(double[][] array, int index) {
    if (isRagged(array))
      throw new IllegalArgumentException("array cannot be ragged");
    double[] vert = new double[array[0].length];
    for (int i = 0; i < vert.length; i++)
      vert[i] = array[i][index];
    if (verbose)
      System.out.println("parsed " + Arrays.toString(vert));
    return vert;
  }

  /**
   * Returns a vertical piece of the passed 2D array in the form of a 1D array.
   * 
   * ex. a 2D array such as [5, 3, 8] [4, 0, 3] [2, 3, 3] when running parseVertArray(array, 1)
   * would return {3, 0, 3}
   * 
   * @param array the array to use
   * @param index the row index to parse
   * @return the parsed array
   * @since 0.1.0.1.0
   */
  public static boolean[] parseVertArray(boolean[][] array, int index) {
    if (isRagged(array))
      throw new IllegalArgumentException("array cannot be ragged");
    boolean[] vert = new boolean[array[0].length];
    for (int i = 0; i < vert.length; i++)
      vert[i] = array[i][index];
    if (verbose)
      println("[+] parsed " + Arrays.toString(vert));
    return vert;
  }

  /**
   * Returns a vertical piece of the passed 2D array in the form of a 1D array.
   * 
   * ex. a 2D array such as [5, 3, 8] [4, 0, 3] [2, 3, 3] when running parseVertArray(array, 1)
   * would return {3, 0, 3}
   * 
   * @param array the array to use
   * @param index the row index to parse
   * @return the parsed array
   * @since 0.1.0.1.0
   */
  public static char[] parseVertArray(char[][] array, int index) {
    if (isRagged(array))
      throw new IllegalArgumentException("array cannot be ragged");
    char[] vert = new char[array[0].length];
    for (int i = 0; i < vert.length; i++)
      vert[i] = array[i][index];
    if (verbose)
      println("[+] parsed " + Arrays.toString(vert));
    return vert;
  }

  /**
   * Gets the indices of every instance of the Object passed in the array passed.
   * 
   * @param array the array to search
   * @param obj the Object to find
   * @return the indicies in an int array
   */
  public static int[] indicesOf(Object[] array, Object obj) {
    ArrayList<Integer> indicies = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i].equals(obj))
        indicies.add(i);
    return toInt(indicies.toArray());
  }

  /**
   * Gets the indices of every instance of the Object passed in the array passed.
   * 
   * @param array the array to search
   * @param obj the Object to find
   * @return the indicies in an int array
   */
  public static int[] indicesOf(int[] array, int obj) {
    ArrayList<Integer> indicies = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        indicies.add(i);
    return toInt(indicies.toArray());
  }

  /**
   * Gets the indices of every instance of the Object passed in the array passed.
   * 
   * @param array the array to search
   * @param obj the Object to find
   * @return the indicies in an int array
   */
  public static int[] indicesOf(double[] array, double obj) {
    ArrayList<Integer> indicies = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        indicies.add(i);
    return toInt(indicies.toArray());
  }

  /**
   * Gets the indices of every instance of the Object passed in the array passed.
   * 
   * @param array the array to search
   * @param obj the Object to find
   * @return the indicies in an int array
   */
  public static int[] indicesOf(char[] array, char obj) {
    ArrayList<Integer> indicies = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        indicies.add(i);
    return toInt(indicies.toArray());
  }

  /**
   * Gets the indices of every instance of the Object passed in the array passed.
   * 
   * @param array the array to search
   * @param obj the Object to find
   * @return the indicies in an int array
   */
  public static int[] indicesOf(boolean[] array, boolean obj) {
    ArrayList<Integer> indicies = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        indicies.add(i);
    return toInt(indicies.toArray());
  }

  /**
   * Finds the dimensions of the given array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to use
   * @return the dimensions
   */
  public static <T> int[] getDims(T[][] array) {
    if (isRagged(array))
      throw new IllegalArgumentException("array must be a table (not ragged)");
    int[] dims = new int[] {array.length, array[0].length};
    if (verbose)
      println("[+] found dimensions: " + dims[0] + "x" + dims[1]);
    return dims;
  }

  /**
   * Finds the dimensions of the given array.
   * 
   * @param array the array to use
   * @return the dimensions
   */
  public static int[] getDims(int[][] array) {
    if (isRagged(array))
      throw new IllegalArgumentException("array must be a table (not ragged)");
    int[] dims = new int[] {array.length, array[0].length};
    if (verbose)
      println("[+] found dimensions: " + dims[0] + "x" + dims[1]);
    return dims;
  }

  /**
   * Finds the dimensions of the given array.
   * 
   * @param array the array to use
   * @return the dimensions
   */
  public static int[] getDims(double[][] array) {
    if (isRagged(array))
      throw new IllegalArgumentException("array must be a table (not ragged)");
    int[] dims = new int[] {array.length, array[0].length};
    if (verbose)
      println("[+] found dimensions: " + dims[0] + "x" + dims[1]);
    return dims;
  }

  /**
   * Finds the dimensions of the given array.
   * 
   * @param array the array to use
   * @return the dimensions
   */
  public static int[] getDims(boolean[][] array) {
    if (isRagged(array))
      throw new IllegalArgumentException("array must be a table (not ragged)");
    int[] dims = new int[] {array.length, array[0].length};
    if (verbose)
      println("[+] found dimensions: " + dims[0] + "x" + dims[1]);
    return dims;
  }

  /**
   * Finds the dimensions of the given array.
   * 
   * @param array the array to use
   * @return the dimensions
   */
  public static int[] getDims(char[][] array) {
    if (isRagged(array))
      throw new IllegalArgumentException("array must be a table (not ragged)");
    int[] dims = new int[] {array.length, array[0].length};
    if (verbose)
      println("[+] found dimensions: " + dims[0] + "x" + dims[1]);
    return dims;
  }

  /**
   * Decides whether a 2D array is ragged or not.
   * 
   * return true = not a perfect table / amount of entries in one subarray is not consistent among
   * all subarrays
   * 
   * @param <T> Generic
   * 
   * @param matrix the matrix to parse
   * @return whether the matrix is ragged or not
   * @since 0.1.0.0.0
   */
  public static <T> boolean isRagged(T[][] matrix) {
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i].length != matrix[j].length)
          return true;
    return false;
  }

  /**
   * Decides whether a 2D array is ragged or not.
   * 
   * return true = not a perfect table / amount of entries in one subarray is not consistent among
   * all subarrays
   * 
   * @param matrix the matrix to parse
   * @return whether the matrix is ragged or not
   * @since 0.1.0.0.0
   */
  public static boolean isRagged(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i].length != matrix[j].length)
          return true;
    return false;
  }

  /**
   * Decides whether a 2D array is ragged or not.
   * 
   * return true = not a perfect table / amount of entries in one subarray is not consistent among
   * all subarrays
   * 
   * @param matrix the matrix to parse
   * @return whether the matrix is ragged or not
   * @since 0.1.0.0.0
   */
  public static boolean isRagged(double[][] matrix) {
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i].length != matrix[j].length)
          return true;
    return false;
  }

  /**
   * Decides whether a 2D array is ragged or not.
   * 
   * return true = not a perfect table / amount of entries in one subarray is not consistent among
   * all subarrays
   * 
   * @param matrix the matrix to parse
   * @return whether the matrix is ragged or not
   * @since 0.1.0.0.0
   */
  public static boolean isRagged(boolean[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        if (matrix[i].length != matrix[j].length) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Decides whether a 2D array is ragged or not.
   * 
   * return true = not a perfect table / amount of entries in one subarray is not consistent among
   * all subarrays
   * 
   * @param matrix the matrix to parse
   * @return whether the matrix is ragged or not
   * @since 0.1.0.0.0
   */
  public static boolean isRagged(char[][] matrix) {
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i].length != matrix[j].length)
          return true;
    return false;
  }

  /**
   * Converts a Point to an array of coordinates.
   * 
   * @param p the Point
   * @return an array of coordinates
   */
  public static int[] coord(Point p) {
    return new int[] {(int) p.getX(), (int) p.getY()};
  }

  /**
   * Gets the position of an element in an array if ArrayUtils cannot be resolved.
   * 
   * @param <T> Generic
   * 
   * @param array the array to use
   * @param obj the object to find
   * @return the index of the element, -1 if not found
   */
  public static <T> int getPosition(T[] array, T obj) {
    for (int i = 0; i < array.length; i++)
      if (array[i].equals(obj))
        return i;
    return -1;
  }

  /**
   * Gets the position of an element in an array if ArrayUtils cannot be resolved.
   * 
   * @param array the array to use
   * @param obj the object to find
   * @return the index of the element, -1 if not found
   */
  public static int getPosition(int[] array, int obj) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == (obj))
        return i;
    return -1;
  }

  /**
   * Gets the position of an element in an array if ArrayUtils cannot be resolved.
   * 
   * @param array the array to use
   * @param obj the object to find
   * @return the index of the element, -1 if not found
   */
  public static int getPosition(double[] array, double obj) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == (obj))
        return i;
    return -1;
  }

  /**
   * Gets the position of an element in an array if ArrayUtils cannot be resolved.
   * 
   * @param array the array to use
   * @param obj the object to find
   * @return the index of the element, -1 if not found
   */
  public static int getPosition(boolean[] array, boolean obj) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == (obj))
        return i;
    return -1;
  }

  /**
   * Gets the position of an element in an array if ArrayUtils cannot be resolved.
   * 
   * @param array the array to use
   * @param obj the object to find
   * @return the index of the element, -1 if not found
   */
  public static int getPosition(char[] array, char obj) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == (obj))
        return i;
    return -1;
  }

  /**
   * Finds the x and y coordinates for the first instance of the specified value in a 2D array.
   * 
   * @param <T> Generic
   * 
   * @param array the array to search
   * @param obj the element to search for
   * @return the elements' coordinates
   */
  public static <T> int[] getPosition(T[][] array, T obj) {
    if (!contains(array, obj))
      throw new IllegalArgumentException("obj specified must be in the array");
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j].equals(obj)) {
          if (verbose)
            println(obj + " found at position [" + i + ", " + j + "]");
          return new int[] {i, j};
        }
    return null;
  }

  /**
   * Finds the x and y coordinates for the first instance of the specified value in a 2D array.
   * 
   * @param array the array to search
   * @param obj the element to search for
   * @return the elements' coordinates
   */
  public static int[] getPosition(int[][] array, int obj) {
    if (!contains(array, obj))
      throw new IllegalArgumentException("obj specified must be in the array");
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj) {
          if (verbose)
            println(obj + " found at position [" + i + ", " + j + "]");
          return new int[] {i, j};
        }
    return null;
  }

  /**
   * Finds the x and y coordinates for the first instance of the specified value in a 2D array.
   * 
   * @param array the array to search
   * @param obj the element to search for
   * @return the elements' coordinates
   */
  public static int[] getPosition(double[][] array, double obj) {
    if (!contains(array, obj))
      throw new IllegalArgumentException("obj specified must be in the array");
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj) {
          if (verbose)
            println(obj + " found at position [" + i + ", " + j + "]");
          return new int[] {i, j};
        }
    return null;
  }

  /**
   * Finds the x and y coordinates for the first instance of the specified value in a 2D array.
   * 
   * @param array the array to search
   * @param obj the element to search for
   * @return the elements' coordinates
   */
  public static int[] getPosition(boolean[][] array, boolean obj) {
    if (!contains(array, obj))
      throw new IllegalArgumentException("obj specified must be in the array");
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj) {
          if (verbose)
            println(obj + " found at position [" + i + ", " + j + "]");
          return new int[] {i, j};
        }
    return null;
  }

  /**
   * Finds the x and y coordinates for the first instance of the specified value in a 2D array.
   * 
   * @param array the array to search
   * @param obj the element to search for
   * @return the elements' coordinates
   */
  public static int[] getPosition(char[][] array, char obj) {
    if (!contains(array, obj))
      throw new IllegalArgumentException("obj specified must be in the array");
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        if (array[i][j] == obj) {
          if (verbose)
            println(obj + " found at position [" + i + ", " + j + "]");
          return new int[] {i, j};
        }
    return null;
  }

  /**
   * Reformats the given array to the specified dimensions.
   * 
   * @param array the array to format
   * @param height the i (y) value
   * @param width the j (x) value
   * @return the newly formatted array
   * @since 0.1.2.3.7
   */
  public static Object[][] reformat(Object[][] array, int height, int width) {
    if (verbose)
      println("[*] reformatting array to " + height + "x" + width);
    return to2DArray(height, width, to1DArray(array));
  }

  /**
   * Reformats the given array to the specified dimensions.
   * 
   * @param array the array to format
   * @param height the i (y) value
   * @param width the j (x) value
   * @return the newly formatted array
   * @since 0.1.2.3.7
   */
  public static int[][] reformat(int[][] array, int height, int width) {
    if (verbose)
      println("[*] reformatting array to " + height + "x" + width);
    return to2DArray(height, width, to1DArray(array));
  }

  /**
   * Reformats the given array to the specified dimensions.
   * 
   * @param array the array to format
   * @param height the i (y) value
   * @param width the j (x) value
   * @return the newly formatted array
   * @since 0.1.2.3.7
   */
  public static double[][] reformat(double[][] array, int height, int width) {
    if (verbose)
      println("[*] reformatting array to " + height + "x" + width);
    return to2DArray(height, width, to1DArray(array));
  }

  /**
   * Reformats the given array to the specified dimensions.
   * 
   * @param array the array to format
   * @param height the i (y) value
   * @param width the j (x) value
   * @return the newly formatted array
   * @since 0.1.2.3.7
   */
  public static boolean[][] reformat(boolean[][] array, int height, int width) {
    if (verbose)
      println("[*] reformatting array to " + height + "x" + width);
    return to2DArray(height, width, to1DArray(array));
  }

  /**
   * Reformats the given array to the specified dimensions.
   * 
   * @param array the array to format
   * @param height the i (y) value
   * @param width the j (x) value
   * @return the newly formatted array
   * @since 0.1.2.3.7
   */
  public static char[][] reformat(char[][] array, int height, int width) {
    if (verbose)
      println("[*] reformatting array to " + height + "x" + width);
    return to2DArray(height, width, to1DArray(array));
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param ints the array to use
   * @return the 2D array
   */
  public static int[][] to2DArray(int[] ints) {
    if (ints.length < 4)
      throw new IllegalArgumentException("array length must be greater than 3");
    if (ints.length % 2 != 0 && ints.length % 3 != 0)
      throw new IllegalArgumentException("array length is prime, cannot convert to 2D array\ntry "
          + "using to2DArray(int rows, int cols, int[] ints)");
    int[][] array = null;
    boolean found = false;
    for (int i = 2; i < 100; i++) {
      for (int j = 2; j < 100; j++) {
        if (i * j == ints.length) {
          array = new int[i][j];
          found = true;
          break;
        }
      }
      if (found)
        break;
    }
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        array[i][j] = ints[(i * array[i].length) + j];
    if (verbose)
      println("[+] converted array to 2D");
    return array;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param array the array to use
   * @return the 2D array
   */
  public static Object[][] to2DArray(Object[] array) {
    if (array.length < 4)
      throw new IllegalArgumentException("array length must be greater than 3");
    if (array.length % 2 != 0 && array.length % 3 != 0)
      throw new IllegalArgumentException("array length is prime, cannot convert to 2D array\ntry "
          + "using to2DArray(int rows, int cols, int[] ints)");
    Object[][] arr = null;
    boolean found = false;
    for (int i = 2; i < 100; i++) {
      for (int j = 2; j < 100; j++) {
        if (i * j == array.length) {
          arr = new Object[i][j];
          found = true;
          break;
        }
      }
      if (found)
        break;
    }
    for (int i = 0; i < arr.length; i++)
      for (int j = 0; j < arr[i].length; j++)
        arr[i][j] = array[(i * arr[i].length) + j];
    if (verbose)
      println("[+] converted array to 2D");
    return arr;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param array the array to use
   * @return the 2D array
   */
  public static boolean[][] to2DArray(boolean[] array) {
    if (array.length < 4)
      throw new IllegalArgumentException("array length must be greater than 3");
    if (array.length % 2 != 0 && array.length % 3 != 0)
      throw new IllegalArgumentException("array length is prime, cannot convert to 2D array\ntry "
          + "using to2DArray(int rows, int cols, int[] ints)");
    boolean[][] arr = null;
    boolean found = false;
    for (int i = 2; i < 100; i++) {
      for (int j = 2; j < 100; j++) {
        if (i * j == array.length) {
          arr = new boolean[i][j];
          found = true;
          break;
        }
      }
      if (found)
        break;
    }
    for (int i = 0; i < arr.length; i++)
      for (int j = 0; j < arr[i].length; j++)
        arr[i][j] = array[(i * arr[i].length) + j];
    if (verbose)
      println("[+] converted array to 2D");
    return arr;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param array the array to use
   * @return the 2D array
   */
  public static double[][] to2DArray(double[] array) {
    if (array.length < 4) {
      throw new IllegalArgumentException("array length must be greater than 3");
    }
    if (array.length % 2 != 0 && array.length % 3 != 0) {
      throw new IllegalArgumentException("array length is prime, cannot convert to 2D array\ntry "
          + "using to2DArray(int rows, int cols, int[] ints)");
    }
    double[][] arr = null;
    boolean found = false;
    for (int i = 2; i < 100; i++) {
      for (int j = 2; j < 100; j++) {
        if (i * j == array.length) {
          arr = new double[i][j];
          found = true;
          break;
        }
      }
      if (found)
        break;
    }
    for (int i = 0; i < arr.length; i++)
      for (int j = 0; j < arr[i].length; j++)
        arr[i][j] = array[(i * arr[i].length) + j];
    if (verbose)
      println("[+] converted array to 2D");
    return arr;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param array the array to use
   * @return the 2D array
   */
  public static char[][] to2DArray(char[] array) {
    if (array.length < 4) {
      throw new IllegalArgumentException("array length must be greater than 3");
    }
    if (array.length % 2 != 0 && array.length % 3 != 0) {
      throw new IllegalArgumentException("array length is prime, cannot convert to 2D array\ntry "
          + "using to2DArray(int rows, int cols, int[] ints)");
    }
    char[][] arr = null;
    boolean found = false;
    for (int i = 2; i < 100; i++) {
      for (int j = 2; j < 100; j++) {
        if (i * j == array.length) {
          arr = new char[i][j];
          found = true;
          break;
        }
      }
      if (found)
        break;
    }
    for (int i = 0; i < arr.length; i++)
      for (int j = 0; j < arr[i].length; j++)
        arr[i][j] = array[(i * arr[i].length) + j];
    if (verbose)
      println("[+] converted array to 2D");
    return arr;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param rows amount of rows in the 2D array
   * @param cols amount of cols in the 2D array
   * @param obj the array to use
   * @return the 2D array
   */
  public static Object[][] to2DArray(int rows, int cols, Object[] array) {
    if (rows * cols != array.length)
      throw new IllegalArgumentException("rows * cols must equals ints.length");
    Object[][] integers = new Object[rows][cols];
    for (int i = 0; i < integers.length; i++)
      for (int j = 0; j < integers[i].length; j++)
        integers[i][j] = array[(i * cols) + j];
    if (verbose)
      println("[+] converted array to a " + rows + " by " + cols);
    return integers;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param rows amount of rows in the 2D array
   * @param cols amount of cols in the 2D array
   * @param ints the array of integers
   * @return the 2D array
   */
  public static int[][] to2DArray(int rows, int cols, int[] array) {
    if (rows * cols != array.length)
      throw new IllegalArgumentException("rows * cols must equals ints.length");
    int[][] integers = new int[rows][cols];
    for (int i = 0; i < integers.length; i++)
      for (int j = 0; j < integers[i].length; j++)
        integers[i][j] = array[(i * cols) + j];
    if (verbose)
      println("[+] converted array to a " + rows + " by " + cols);
    return integers;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param rows amount of rows in the 2D array
   * @param cols amount of cols in the 2D array
   * @param array the array to use
   * @return the 2D array
   */
  public static double[][] to2DArray(int rows, int cols, double[] array) {
    if (rows * cols != array.length)
      throw new IllegalArgumentException("rows * cols must equals ints.length");
    double[][] integers = new double[rows][cols];
    for (int i = 0; i < integers.length; i++)
      for (int j = 0; j < integers[i].length; j++)
        integers[i][j] = array[(i * cols) + j];
    if (verbose)
      println("[+] converted array to a " + rows + " by " + cols);
    return integers;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param rows amount of rows in the 2D array
   * @param cols amount of cols in the 2D array
   * @param obj the array to use
   * @return the 2D array
   */
  public static boolean[][] to2DArray(int rows, int cols, boolean[] array) {
    if (rows * cols != array.length)
      throw new IllegalArgumentException("rows * cols must equals ints.length");
    boolean[][] integers = new boolean[rows][cols];
    for (int i = 0; i < integers.length; i++)
      for (int j = 0; j < integers[i].length; j++)
        integers[i][j] = array[(i * cols) + j];
    if (verbose)
      println("[+] converted array to a " + rows + " by " + cols);
    return integers;
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param rows amount of rows in the 2D array
   * @param cols amount of cols in the 2D array
   * @param chars the array of integers
   * @return the 2D array
   */
  public static char[][] to2DArray(int rows, int cols, char[] array) {
    if (rows * cols != array.length)
      throw new IllegalArgumentException("rows * cols must equals ints.length");
    char[][] integers = new char[rows][cols];
    for (int i = 0; i < integers.length; i++)
      for (int j = 0; j < integers[i].length; j++)
        integers[i][j] = array[(i * cols) + j];
    if (verbose)
      println("[+] converted array to a " + rows + " by " + cols);
    return integers;
  }

  /**
   * Creates a 1D array from a 2D array.
   * 
   * ex. {{5, 4, 7}, {3, 9, 5}} would become: {5, 4, 7, 3, 9, 5}
   * 
   * @param array the array to use
   * @return the 1D array
   */
  public static Object[] to1DArray(Object[][] array) {
    int leng = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        leng++;
    Object[] oneD = new Object[leng];
    int index = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++) {
        oneD[index] = array[i][j];
        index++;
      }
    if (verbose)
      println("[+] converted 2D array to 1D at length " + oneD.length);
    return oneD;
  }

  /**
   * Creates a 1D array from a 2D array.
   * 
   * ex. {{5, 4, 7}, {3, 9, 5}} would become: {5, 4, 7, 3, 9, 5}
   * 
   * @param array the array to use
   * @return the 1D array
   */
  public static int[] to1DArray(int[][] array) {
    int leng = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        leng++;
    int[] oneD = new int[leng];
    int index = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++) {
        oneD[index] = array[i][j];
        index++;
      }
    if (verbose)
      println("[+] converted 2D array to 1D at length " + oneD.length);
    return oneD;
  }

  /**
   * Creates a 1D array from a 2D array.
   * 
   * ex. {{5, 4, 7}, {3, 9, 5}} would become: {5, 4, 7, 3, 9, 5}
   * 
   * @param array the array to use
   * @return the 1D array
   */
  public static double[] to1DArray(double[][] array) {
    int leng = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        leng++;
    double[] oneD = new double[leng];
    int index = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++) {
        oneD[index] = array[i][j];
        index++;
      }
    if (verbose)
      println("[+] converted 2D array to 1D at length " + oneD.length);
    return oneD;
  }

  /**
   * Creates a 1D array from a 2D array.
   * 
   * ex. {{5, 4, 7}, {3, 9, 5}} would become: {5, 4, 7, 3, 9, 5}
   * 
   * @param array the array to use
   * @return the 1D array
   */
  public static boolean[] to1DArray(boolean[][] array) {
    int leng = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        leng++;
    boolean[] oneD = new boolean[leng];
    int index = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++) {
        oneD[index] = array[i][j];
        index++;
      }
    if (verbose)
      println("[+] converted 2D array to 1D at length " + oneD.length);
    return oneD;
  }

  /**
   * Creates a 1D array from a 2D array.
   * 
   * ex. {{5, 4, 7}, {3, 9, 5}} would become: {5, 4, 7, 3, 9, 5}
   * 
   * @param array the array to use
   * @return the 1D array
   */
  public static char[] to1DArray(char[][] array) {
    int leng = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++)
        leng++;
    char[] oneD = new char[leng];
    int index = 0;
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array[i].length; j++) {
        oneD[index] = array[i][j];
        index++;
      }
    if (verbose)
      println("[+] converted 2D array to 1D at length " + oneD.length);
    return oneD;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Object, Integer> freqMap(Object[] array) {
    HashMap<Object, Integer> map = new HashMap<>();
    for (Object i : array)
      map.put(i, countFreq(array, i));
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Integer, Integer> freqMap(int[] array) {
    HashMap<Integer, Integer> map = new HashMap<>();
    for (int i : array)
      map.put(i, countFreq(array, i));
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Double, Integer> freqMap(double[] array) {
    HashMap<Double, Integer> map = new HashMap<>();
    for (double i : array)
      map.put(i, countFreq(array, i));
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Character, Integer> freqMap(char[] array) {
    HashMap<Character, Integer> map = new HashMap<>();
    for (char i : array)
      map.put(i, countFreq(array, i));
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Boolean, Integer> freqMap(boolean[] array) {
    HashMap<Boolean, Integer> map = new HashMap<>();
    for (boolean i : array)
      map.put(i, countFreq(array, i));
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Object, Integer> freqMap(Object[][] array) {
    HashMap<Object, Integer> map = new HashMap<>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        map.put(array[i][j], countFreq(array, array[i][j]));
      }
    }
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Integer, Integer> freqMap(int[][] array) {
    HashMap<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        map.put(array[i][j], countFreq(array, array[i][j]));
      }
    }
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Double, Integer> freqMap(double[][] array) {
    HashMap<Double, Integer> map = new HashMap<>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        map.put(array[i][j], countFreq(array, array[i][j]));
      }
    }
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Character, Integer> freqMap(char[][] array) {
    HashMap<Character, Integer> map = new HashMap<>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        map.put(array[i][j], countFreq(array, array[i][j]));
      }
    }
    return map;
  }

  /**
   * Gets the frequency of every element in the array.
   * 
   * @param array the array to search
   * @return the frequency map
   */
  public static HashMap<Boolean, Integer> freqMap(boolean[][] array) {
    HashMap<Boolean, Integer> map = new HashMap<>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        map.put(array[i][j], countFreq(array, array[i][j]));
      }
    }
    return map;
  }

  /**
   * Gets the Entry from a TreeMap at a certain index.
   * 
   * @param <K> the key
   * @param <V> the value
   * 
   * @param map the map to search
   * @return the Entry at the index
   * @since 0.1.5.1.6
   */
  public static <K, V> Map.Entry<K, V> getAt(int index, TreeMap<K, V> map) {
    int count = 0;
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (count == index)
        return entry;
      count++;
    }
    return null;
  }

  /**
   * Gets a Set of all values in a Map.
   * 
   * @param <K> k
   * @param <V> v
   * @param map the map
   * @return a set of values
   * @since 0.1.5.3.1
   */
  public static <K, V> ArrayList<V> valueList(Map<K, V> map) {
    ArrayList<V> list = new ArrayList<>();
    map.entrySet().forEach(e -> list.add(e.getValue()));
    return list;
  }

  // -------------------------SET OPERATIONS----------------------------------

  /**
   * Checks if the given array has duplicates.
   * 
   * @param array the array to use
   * @return whether 'array' contains duplicate values
   * @since 0.1.4.2.3
   */
  public static boolean containsDups(Object[] array) {
    for (Object obj : array)
      if (countFreq(array, obj) > 1)
        return true;
    return false;
  }

  /**
   * Checks if the given array has duplicates.
   * 
   * @param array the array to use
   * @return whether 'array' contains duplicate values
   * @since 0.1.4.2.3
   */
  public static boolean containsDups(int[] array) {
    for (int obj : array)
      if (countFreq(array, obj) > 1)
        return true;
    return false;
  }

  /**
   * Checks if the given array has duplicates.
   * 
   * @param array the array to use
   * @return whether 'array' contains duplicate values
   * @since 0.1.4.2.3
   */
  public static boolean containsDups(double[] array) {
    for (double obj : array)
      if (countFreq(array, obj) > 1)
        return true;
    return false;
  }

  /**
   * Checks if the given array has duplicates.
   * 
   * @param array the array to use
   * @return whether 'array' contains duplicate values
   * @since 0.1.4.2.3
   */
  public static boolean containsDups(boolean[] array) {
    for (boolean obj : array)
      if (countFreq(array, obj) > 1)
        return true;
    return false;
  }

  /**
   * Checks if the given array has duplicates.
   * 
   * @param array the array to use
   * @return whether 'array' contains duplicate values
   * @since 0.1.4.2.3
   */
  public static boolean containsDups(char[] array) {
    for (char obj : array)
      if (countFreq(array, obj) > 1)
        return true;
    return false;
  }

  /**
   * Finds duplicate values in an array and returns them as another array.
   * 
   * @param array the array to check for duplicates
   * @return a new array of duplicates within the array specified
   * @since 0.1.4.2.3
   */
  public static Object[] getDups(Object[] array) {
    ArrayList<Object> noDups = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array.length; j++)
        if (i != j && !contains(noDups.toArray(), array[i]) && array[i].equals(array[j]))
          noDups.add(array[j]);
    if (verbose)
      println("[+] found " + noDups.size() + " dups: " + Arrays.toString(noDups.toArray()));
    return noDups.toArray();
  }

  /**
   * Finds duplicate values in an array and returns them as another array.
   * 
   * @param array the array to check for duplicates
   * @return a new array of duplicates within the array specified
   * @since 0.1.4.2.3
   */
  public static int[] getDups(int[] array) {
    ArrayList<Integer> noDups = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array.length; j++)
        if (i != j && !contains(noDups.toArray(), array[i]) && array[i] == array[j])
          noDups.add(array[j]);
    if (verbose)
      println("[+] found " + noDups.size() + " dups: " + Arrays.toString(noDups.toArray()));
    return toInt(noDups.toArray());
  }

  /**
   * Finds duplicate values in an array and returns them as another array.
   * 
   * @param array the array to check for duplicates
   * @return a new array of duplicates within the array specified
   * @since 0.1.4.2.3
   */
  public static double[] getDups(double[] array) {
    ArrayList<Double> noDups = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array.length; j++)
        if (i != j && !contains(noDups.toArray(), array[i]) && array[i] == array[j])
          noDups.add(array[j]);
    if (verbose)
      println("[+] found " + noDups.size() + " dups: " + Arrays.toString(noDups.toArray()));
    return toDouble(noDups.toArray());
  }

  /**
   * Finds duplicate values in an array and returns them as another array.
   * 
   * @param array the array to check for duplicates
   * @return a new array of duplicates within the array specified
   * @since 0.1.4.2.3
   */
  public static char[] getDups(char[] array) {
    ArrayList<Character> noDups = new ArrayList<>();
    for (int i = 0; i < array.length; i++)
      for (int j = 0; j < array.length; j++)
        if (i != j && !contains(noDups.toArray(), array[i]) && array[i] == array[j])
          noDups.add(array[j]);
    if (verbose)
      println("[+] found " + noDups.size() + " dups: " + Arrays.toString(noDups.toArray()));
    return toChar(noDups.toArray());
  }

  /**
   * Removes all duplicates in the array given.
   * 
   * @param array the array to use
   * @param reorder whether to reorder the array to proper order after removing duplications
   * @return the array with no duplicates
   * @since 0.1.4.2.3
   */
  public static Object[] removeDups(Object[] list, boolean reorder) {
    if (verbose)
      println("[*] removing duplicates");
    if (reorder)
      return reorder(new HashSet<>(Arrays.asList(list)).toArray(), list);
    return new HashSet<>(Arrays.asList(list)).toArray();
  }

  /**
   * Removes all duplicates in the array given.
   * 
   * @param array the array to use
   * @param reorder whether to reorder the array to proper order after removing duplications
   * @return the array with no duplicates
   * @since 0.1.4.2.3
   */
  public static int[] removeDups(int[] list, boolean reorder) {
    if (verbose)
      println("[*] removing duplicates");
    if (reorder)
      return reorder(toInt(new HashSet<>(List.of(box(list))).toArray()), list);
    return toInt(new HashSet<>(List.of(box(list))).toArray());
  }

  /**
   * Removes all duplicates in the array given.
   * 
   * @param array the array to use
   * @param reorder whether to reorder the array to proper order after removing duplications
   * @return the array with no duplicates
   * @since 0.1.4.2.3
   */
  public static double[] removeDups(double[] list, boolean reorder) {
    if (verbose)
      println("[*] removing duplicates");
    if (reorder)
      return reorder(toDouble(new HashSet<>(List.of(box(list))).toArray()), list);
    return toDouble(new HashSet<>(List.of(box(list))).toArray());
  }

  /**
   * Removes all duplicates in the array given.
   * 
   * @param array the array to use
   * @param reorder whether to reorder the array to proper order after removing duplications
   * @return the array with no duplicates
   * @since 0.1.4.2.3
   */
  public static char[] removeDups(char[] list, boolean reorder) {
    if (verbose)
      println("[*] removing duplicates");
    if (reorder)
      return reorder(toChar(new HashSet<>(List.of(box(list))).toArray()), list);
    return toChar(new HashSet<>(List.of(box(list))).toArray());
  }

  /**
   * Concatenates multiple arrays with the choice of removing duplicates.
   * 
   * 
   * @param removeDups whether to remove duplicates or not
   * @param arrays the arrays to concatenate
   * @return the unionized arrays
   * @since 0.1.5.3.1
   */
  public static Object[] union(boolean removeDups, Object[]... arrays) {
    ArrayList<Object> unionized = new ArrayList<>();
    for (int i = 0; i < arrays.length; i++)
      for (int j = 0; j < arrays[i].length; j++)
        unionized.add(arrays[i][j]);
    Object[] union = unionized.toArray();
    if (removeDups)
      return removeDups(union, true);
    return union;
  }

  /**
   * Concatenates multiple arrays with the choice of removing duplicates.
   * 
   * 
   * @param removeDups whether to remove duplicates or not
   * @param arrays the arrays to concatenate
   * @return the unionized arrays
   * @since 0.1.5.3.1
   */
  public static int[] union(boolean removeDups, int[]... arrays) {
    ArrayList<Integer> unionized = new ArrayList<>();
    for (int i = 0; i < arrays.length; i++)
      for (int j = 0; j < arrays[i].length; j++)
        unionized.add(arrays[i][j]);
    if (removeDups)
      return toInt(removeDups(unionized.toArray(), true));
    return toInt(unionized.toArray());
  }

  /**
   * Concatenates multiple arrays with the choice of removing duplicates.
   * 
   * 
   * @param removeDups whether to remove duplicates or not
   * @param arrays the arrays to concatenate
   * @return the unionized arrays
   * @since 0.1.5.3.1
   */
  public static double[] union(boolean removeDups, double[]... arrays) {
    ArrayList<Double> unionized = new ArrayList<>();
    for (int i = 0; i < arrays.length; i++)
      for (int j = 0; j < arrays[i].length; j++)
        unionized.add(arrays[i][j]);
    if (removeDups)
      return toDouble(removeDups(unionized.toArray(), true));
    return toDouble(unionized.toArray());
  }

  /**
   * Concatenates multiple arrays with the choice of removing duplicates.
   * 
   * 
   * @param removeDups whether to remove duplicates or not
   * @param arrays the arrays to concatenate
   * @return the unionized arrays
   * @since 0.1.5.3.1
   */
  public static char[] union(boolean removeDups, char[]... arrays) {
    ArrayList<Character> unionized = new ArrayList<>();
    for (int i = 0; i < arrays.length; i++)
      for (int j = 0; j < arrays[i].length; j++)
        unionized.add(arrays[i][j]);
    if (removeDups)
      return toChar(removeDups(unionized.toArray(), true));
    return toChar(unionized.toArray());
  }

  /**
   * Finds the similarities between the arrays passed.
   * 
   * @return the intersected array
   * @since 0.1.5.3.1
   */
  public static Object[] intersect(Object[]... arrays) {
    ArrayList<Object> retained = new ArrayList<>(List.of(arrays[0]));
    for (int i = 1; i < arrays.length; i++)
      retained.retainAll(List.of(arrays[i]));
    if (verbose)
      println("[+] intersect yielded " + Arrays.toString(retained.toArray()));
    return retained.toArray();
  }

  /**
   * Finds the similarities between the arrays passed.
   * 
   * @return the intersected array
   * @since 0.1.5.3.1
   */
  public static int[] intersect(int[]... arrays) {
    ArrayList<Integer> retained = new ArrayList<>();
    importAll(arrays[0], retained);
    for (int i = 1; i < arrays.length; i++)
      retained.retainAll(List.of(box(arrays[i])));
    if (verbose)
      println("[+] intersect yielded " + Arrays.toString(retained.toArray()));
    return toInt(retained.toArray());
  }

  /**
   * Finds the similarities between the arrays passed.
   * 
   * @return the intersected array
   * @since 0.1.5.3.1
   */
  public static double[] intersect(double[]... arrays) {
    ArrayList<Double> retained = new ArrayList<>();
    importAll(arrays[0], retained);
    for (int i = 1; i < arrays.length; i++)
      retained.retainAll(List.of(box(arrays[i])));
    if (verbose)
      println("[+] intersect yielded " + Arrays.toString(retained.toArray()));
    return toDouble(retained.toArray());
  }

  /**
   * Finds the similarities between the arrays passed.
   * 
   * @return the intersected array
   * @since 0.1.5.3.1
   */
  public static char[] intersect(char[]... arrays) {
    ArrayList<Character> retained = new ArrayList<>();
    importAll(arrays[0], retained);
    for (int i = 1; i < arrays.length; i++)
      retained.retainAll(List.of(box(arrays[i])));
    if (verbose)
      println("[+] intersect yielded " + Arrays.toString(retained.toArray()));
    return toChar(retained.toArray());
  }

  /**
   * Attempts to reorder an array according to a correctly ordered array.
   * 
   * @param toReorder the array to reorder
   * @param reference the reference array
   * @return the reordered array
   * @since 0.1.5.3.1
   */
  public static Object[] reorder(Object[] toReorder, Object[] reference) {
    TreeMap<Integer, Object> reorderedmap = new TreeMap<>();
    for (int i = 0; i < toReorder.length; i++)
      reorderedmap.put(getPosition(reference, toReorder[i]), toReorder[i]);
    ArrayList<Object> fixed = new ArrayList<>();
    for (Map.Entry<Integer, Object> entry : reorderedmap.entrySet())
      fixed.add(entry.getValue());
    return fixed.toArray();
  }

  /**
   * Attempts to reorder an array according to a correctly ordered array.
   * 
   * @param toReorder the array to reorder
   * @param reference the reference array
   * @return the reordered array
   * @since 0.1.5.3.1
   */
  public static int[] reorder(int[] toReorder, int[] reference) {
    TreeMap<Integer, Integer> reorderedmap = new TreeMap<>();
    for (int i = 0; i < toReorder.length; i++)
      reorderedmap.put(getPosition(reference, toReorder[i]), toReorder[i]);
    ArrayList<Integer> fixed = new ArrayList<>();
    for (Map.Entry<Integer, Integer> entry : reorderedmap.entrySet())
      fixed.add(entry.getValue());
    return toInt(fixed.toArray());
  }

  /**
   * Attempts to reorder an array according to a correctly ordered array.
   * 
   * @param toReorder the array to reorder
   * @param reference the reference array
   * @return the reordered array
   * @since 0.1.5.3.1
   */
  public static double[] reorder(double[] toReorder, double[] reference) {
    TreeMap<Integer, Double> reorderedmap = new TreeMap<>();
    for (int i = 0; i < toReorder.length; i++)
      reorderedmap.put(getPosition(reference, toReorder[i]), toReorder[i]);
    ArrayList<Double> fixed = new ArrayList<>();
    for (Map.Entry<Integer, Double> entry : reorderedmap.entrySet())
      fixed.add(entry.getValue());
    return toDouble(fixed.toArray());
  }

  /**
   * Attempts to reorder an array according to a correctly ordered array.
   * 
   * @param toReorder the array to reorder
   * @param reference the reference array
   * @return the reordered array
   * @since 0.1.5.3.1
   */
  public static char[] reorder(char[] toReorder, char[] reference) {
    TreeMap<Integer, Character> reorderedmap = new TreeMap<>();
    for (int i = 0; i < toReorder.length; i++)
      reorderedmap.put(getPosition(reference, toReorder[i]), toReorder[i]);
    ArrayList<Character> fixed = new ArrayList<>();
    for (Map.Entry<Integer, Character> entry : reorderedmap.entrySet())
      fixed.add(entry.getValue());
    return toChar(fixed.toArray());
  }

  // -----------------------------TYPE CONVERSION---------------------------------

  /**
   * Converts a primitive array into its respective Object array.
   * 
   * @param array the array to convert
   * @return the Object array
   * @since 0.1.2.3.7
   */
  public static Integer[] box(int[] array) {
    Integer[] arr = new Integer[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts a primitive array into its respective Object array.
   * 
   * @param array the array to convert
   * @return the Object array
   * @since 0.1.2.3.7
   */
  public static Double[] box(double[] array) {
    Double[] arr = new Double[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts a primitive array into its respective Object array.
   * 
   * @param array the array to convert
   * @return the Object array
   * @since 0.1.2.3.7
   */
  public static Boolean[] box(boolean[] array) {
    Boolean[] arr = new Boolean[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts a primitive array into its respective Object array.
   * 
   * @param array the array to convert
   * @return the Object array
   * @since 0.1.2.3.7
   */
  public static Character[] box(char[] array) {
    Character[] arr = new Character[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts a primitive array into its respective Object array.
   * 
   * @param array the array to convert
   * @return the Object array
   * @since 0.1.5.3.5
   */
  public static Integer[][] box(int[][] array) {
    Integer[][] destination = new Integer[array.length][];
    for (int i = 0; i < destination.length; i++) {
      destination[i] = new Integer[array[i].length];
      for (int j = 0; j < destination[i].length; j++)
        destination[i][j] = array[i][j];
    }
    return destination;
  }

  /**
   * Converts a primitive array into its respective Object array.
   * 
   * @param array the array to convert
   * @return the Object array
   * @since 0.1.5.3.5
   */
  public static Double[][] box(double[][] array) {
    Double[][] destination = new Double[array.length][];
    for (int i = 0; i < destination.length; i++) {
      destination[i] = new Double[array[i].length];
      for (int j = 0; j < destination[i].length; j++)
        destination[i][j] = array[i][j];
    }
    return destination;
  }

  /**
   * Converts an Object array into its respective primitive array.
   * 
   * @param array the array to convert
   * @return the converted array
   * @since 0.1.2.3.7
   */
  public static int[] unbox(Integer[] array) {
    int[] arr = new int[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts an Object array into its respective primitive array.
   * 
   * @param array the array to convert
   * @return the converted array
   * @since 0.1.2.3.7
   */
  public static double[] unbox(Double[] array) {
    double[] arr = new double[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts an Object array into its respective primitive array.
   * 
   * @param array the array to convert
   * @return the converted array
   * @since 0.1.2.3.7
   */
  public static boolean[] unbox(Boolean[] array) {
    boolean[] arr = new boolean[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts an Object array into its respective primitive array.
   * 
   * @param array the array to convert
   * @return the converted array
   * @since 0.1.2.3.7
   */
  public static char[] unbox(Character[] array) {
    char[] arr = new char[array.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = array[i];
    return arr;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static int[] toInt(Object[] array) {
    int[] fixed = new int[array.length];
    for (int i = 0; i < fixed.length; i++)
      fixed[i] = (int) array[i];
    return fixed;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static double[] toDouble(Object[] array) {
    double[] fixed = new double[array.length];
    for (int i = 0; i < fixed.length; i++)
      fixed[i] = (double) array[i];
    return fixed;
  }
  
  /**
   * Converts an array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static double[] toDouble(int[] array) {
    double[] fixed = new double[array.length];
    for (int i = 0; i < fixed.length; i++)
      fixed[i] = array[i];
    return fixed;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static char[] toChar(Object[] array) {
    char[] fixed = new char[array.length];
    for (int i = 0; i < fixed.length; i++)
      fixed[i] = (char) array[i];
    return fixed;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static boolean[] toBoolean(Object[] array) {
    boolean[] fixed = new boolean[array.length];
    for (int i = 0; i < fixed.length; i++)
      fixed[i] = (boolean) array[i];
    return fixed;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static int[][] toInt(Object[][] array) {
    int[][] destination = new int[array.length][];
    for (int i = 0; i < destination.length; ++i) {
      destination[i] = new int[array[i].length];
      for (int j = 0; j < destination[i].length; ++j)
        destination[i][j] = (int) array[i][j];
    }
    return destination;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static double[][] toDouble(Object[][] array) {
    double[][] destination = new double[array.length][];
    for (int i = 0; i < destination.length; ++i) {
      destination[i] = new double[array[i].length];
      for (int j = 0; j < destination[i].length; ++j)
        destination[i][j] = (double) array[i][j];
    }
    return destination;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static char[][] toChar(Object[][] array) {
    char[][] destination = new char[array.length][];
    for (int i = 0; i < destination.length; ++i) {
      destination[i] = new char[array[i].length];
      for (int j = 0; j < destination[i].length; ++j)
        destination[i][j] = (char) array[i][j];
    }
    return destination;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static boolean[][] toBoolean(Object[][] array) {
    boolean[][] destination = new boolean[array.length][];
    for (int i = 0; i < destination.length; ++i) {
      destination[i] = new boolean[array[i].length];
      for (int j = 0; j < destination[i].length; ++j)
        destination[i][j] = (boolean) array[i][j];
    }
    return destination;
  }

  /**
   * Converts the array specified to the array type requested.
   * 
   * @param array the array
   * @return the fixed array
   */
  public static int[] toInt(double[] array) {
    int[] a = new int[array.length];
    for (int i = 0; i < a.length; i++)
      a[i] = (int) array[i];
    return a;
  }

  /**
   * Converts the array specified to the array type requested.
   * 
   * @param array the array
   * @return the fixed array
   * @since 0.1.5.3.5
   */
  public static int[][] toInt(double[][] array) {
    int[][] destination = new int[array.length][];
    for (int i = 0; i < destination.length; ++i) {
      destination[i] = new int[array[i].length];
      for (int j = 0; j < destination[i].length; ++j)
        destination[i][j] = (int) array[i][j];
    }
    return destination;
  }

  /**
   * Converts the array specified to the array type requested.
   * 
   * @param array the array
   * @return the fixed array
   * @since 0.1.5.3.5
   */
  public static double[][] toDouble(int[][] array) {
    double[][] destination = new double[array.length][];
    for (int i = 0; i < destination.length; ++i) {
      destination[i] = new double[array[i].length];
      for (int j = 0; j < destination[i].length; ++j)
        destination[i][j] = array[i][j];
    }
    return destination;
  }

  // ---------------------------------GUI-------------------------------------

  /**
   * Quick and customizable creation of a JFrame.
   * 
   * @param contentPaneBG the background color of the content pane
   * @param mainPanelBG the background color of the main panel
   * @param width the width of the frame
   * @param height the height of the frame
   * @param centerInWindow whether to center the frame in the middle of the screen or not
   * @return the customized frame
   * @since 0.1.5.3.0
   */
  public static JFrame buildFrame(Color contentPaneBG, Color mainPanelBG, int width, int height,
      boolean centerInWindow) {
    JFrame frame = new JFrame();
    JPanel contentPane;
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(100, 100, width, height);
    contentPane = new JPanel();
    contentPane.setBackground(contentPaneBG);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    frame.setContentPane(contentPane);
    JPanel panel = new JPanel();
    panel.setBackground(mainPanelBG);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE));
    contentPane.setLayout(gl_contentPane);
    frame.pack();
    if (centerInWindow)
      frame.setLocationRelativeTo(null);
    return frame;
  }

  /**
   * Draws a centered String within the bounds specified.
   * 
   * @param page the Graphics2D object
   * @param s the String to draw
   * @param x the x
   * @param y the y
   * @param width the width
   * @param height the height
   * @since 0.1.4.4.1
   */
  public static void drawCenteredString(Graphics2D page, String s, int x, int y, int width,
      int height) {
    java.awt.FontMetrics fm = page.getFontMetrics(page.getFont());
    java.awt.geom.Rectangle2D rect = fm.getStringBounds(s, page);
    int textHeight = (int) (rect.getHeight());
    int textWidth = (int) (rect.getWidth());
    int textX = x + (width - textWidth) / 2;
    int textY = y + (height - textHeight) / 2 + fm.getAscent();
    page.drawString(s, textX, textY);
  }

  /**
   * Centers a Frame in the middle of the screen.
   * 
   * @param j the frame to center
   * @param pack whether to pack the frame before centering
   * @since 0.1.5.3.0
   */
  public static void centerFrame(JFrame j, boolean pack) {
    if (pack)
      j.pack();
    j.setLocationRelativeTo(null);
  }

  /**
   * Safely converts an Image to a BufferedImage.
   * 
   * @param image the image to convert
   * @return a new BufferedImage
   * @since 0.1.4.4.1
   */
  public static BufferedImage toBuffered(Image image) {
    BufferedImage newImage =
        new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
  }

  /**
   * Copies a BufferedImage.
   * 
   * @param toCopy the BufferedImage to copy
   * @return the fresh BufferedImage copy
   * @since 0.1.4.4.1
   */
  public static BufferedImage copyOf(BufferedImage toCopy) {
    ColorModel cm = toCopy.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = toCopy.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }

  /**
   * Gets the dimensions of the current screen
   * 
   * @return the dimensions of this screen
   * @since 0.1.4.4.1
   */
  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  /**
   * Calculates the angle between two points.
   * 
   * @author MadProgrammer on StackOverflow
   * @param from the Point2D from
   * @param to the Point2D to
   * @return the angle
   */
  protected double angleBetween(Point2D from, Point2D to) {
    double x = from.getX();
    double y = from.getY();
    double deltaX = to.getX() - x;
    double deltaY = to.getY() - y;
    double rotation = -Math.atan2(deltaX, deltaY);
    rotation = Math.toRadians(Math.toDegrees(rotation) + 180);
    return rotation;
  }

  /**
   * Retrieves a point on a circle.
   * 
   * @author MadProgrammer on StackOverflow
   * @param center the center
   * @param radians radians
   * @param radius the radius
   * @return the point
   */
  protected Point2D getPointOnCircle(Point2D center, double radians, double radius) {
    double x = center.getX();
    double y = center.getY();
    radians = radians - Math.toRadians(90.0);
    double xPosy = Math.round((float) (x + Math.cos(radians) * radius));
    double yPosy = Math.round((float) (y + Math.sin(radians) * radius));
    return new Point2D.Double(xPosy, yPosy);
  }

  // ------------------------2D ARRAY DIRECTIONAL-----------------------------

  /**
   * Checks whether the passed coordinates have an element above it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element above it
   */
  public static boolean hasAbove(Object[][] array, int[] coord) {
    try {
      Object above = array[coord[0] - 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element above it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element above it
   */
  public static boolean hasAbove(int[][] array, int[] coord) {
    try {
      int above = array[coord[0] - 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element above it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element above it
   */
  public static boolean hasAbove(double[][] array, int[] coord) {
    try {
      double above = array[coord[0] - 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element above it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element above it
   */
  public static boolean hasAbove(boolean[][] array, int[] coord) {
    try {
      boolean above = array[coord[0] - 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element above it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element above it
   */
  public static boolean hasAbove(char[][] array, int[] coord) {
    try {
      char above = array[coord[0] - 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element below it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element below it
   */
  public static boolean hasBelow(Object[][] array, int[] coord) {
    try {
      Object below = array[coord[0] + 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element below it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element below it
   */
  public static boolean hasBelow(int[][] array, int[] coord) {
    try {
      int below = array[coord[0] + 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element below it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element below it
   */
  public static boolean hasBelow(double[][] array, int[] coord) {
    try {
      double below = array[coord[0] + 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element below it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element below it
   */
  public static boolean hasBelow(boolean[][] array, int[] coord) {
    try {
      boolean below = array[coord[0] + 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element below it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element below it
   */
  public static boolean hasBelow(char[][] array, int[] coord) {
    try {
      char below = array[coord[0] + 1][coord[1]];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the right of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the right of it
   */
  public static boolean hasRight(Object[][] array, int[] coord) {
    if (!contains(array, array[coord[0]][coord[1]])) {
      throw new IllegalArgumentException("element specified must be in the array");
    }
    try {
      Object below = array[coord[0]][coord[1] + 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the right of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the right of it
   */
  public static boolean hasRight(int[][] array, int[] coord) {
    try {
      int below = array[coord[0]][coord[1] + 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the right of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the right of it
   */
  public static boolean hasRight(double[][] array, int[] coord) {
    try {
      double below = array[coord[0]][coord[1] + 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the right of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the right of it
   */
  public static boolean hasRight(boolean[][] array, int[] coord) {
    try {
      boolean below = array[coord[0]][coord[1] + 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the right of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the right of it
   */
  public static boolean hasRight(char[][] array, int[] coord) {
    try {
      char below = array[coord[0]][coord[1] + 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the left of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the left of it
   */
  public static boolean hasLeft(Object[][] array, int[] coord) {
    try {
      Object below = array[coord[0]][coord[1] - 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the left of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the left of it
   */
  public static boolean hasLeft(int[][] array, int[] coord) {
    try {
      int below = array[coord[0]][coord[1] - 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the left of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the left of it
   */
  public static boolean hasLeft(double[][] array, int[] coord) {
    try {
      double below = array[coord[0]][coord[1] - 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the left of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the left of it
   */
  public static boolean hasLeft(boolean[][] array, int[] coord) {
    if (!contains(array, array[coord[0]][coord[1]])) {
      throw new IllegalArgumentException("element specified must be in the array");
    }
    try {
      boolean below = array[coord[0]][coord[1] - 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element to the left of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element to the left of it
   */
  public static boolean hasLeft(char[][] array, int[] coord) {
    try {
      char below = array[coord[0]][coord[1] - 1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNw(Object[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNw(int[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNw(double[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNw(boolean[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNw(char[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNe(Object[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNe(int[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNe(double[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNe(boolean[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element northwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element northwest of it
   */
  public static boolean hasNe(char[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] - 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southeast of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southeast of it
   */
  public static boolean hasSe(Object[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southeast of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southeast of it
   */
  public static boolean hasSe(int[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southeast of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southeast of it
   */
  public static boolean hasSe(double[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southeast of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southeast of it
   */
  public static boolean hasSe(boolean[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southeast of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southeast of it
   */
  public static boolean hasSe(char[][] array, int[] coord) {
    if (hasRight(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] + 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southwest of it
   */
  public static boolean hasSw(Object[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southwest of it
   */
  public static boolean hasSw(int[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southwest of it
   */
  public static boolean hasSw(double[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southwest of it
   */
  public static boolean hasSw(boolean[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Checks whether the passed coordinates have an element southwest of it in the 2D array.
   * 
   * coord[0] = i (y) : coord[1] = j (x)
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return whether the element at the specified coordinates has an element southwest of it
   */
  public static boolean hasSw(char[][] array, int[] coord) {
    if (hasLeft(array, coord)) {
      try {
        Object nw = array[coord[0] + 1][coord[1] - 1];
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Returns the element immediately above the coordinates specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element above the given coordinates
   */
  public static Object above(Object[][] array, int[] coord) {
    if (!hasAbove(array, coord)) {
      return null;
    }
    if (verbose) {
      System.out.println(array[coord[0] - 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] - 1][coord[1]];
  }

  /**
   * Returns the element immediately above the coordinates specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element above the given coordinates
   */
  public static int above(int[][] array, int[] coord) {
    if (!hasAbove(array, coord)) {
      return Integer.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(array[coord[0] - 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] - 1][coord[1]];
  }

  /**
   * Returns the element immediately above the coordinates specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element above the given coordinates
   */
  public static double above(double[][] array, int[] coord) {
    if (!hasAbove(array, coord)) {
      return Double.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(array[coord[0] - 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] - 1][coord[1]];
  }

  /**
   * Returns the element immediately above the coordinates specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element above the given coordinates
   */
  public static boolean above(boolean[][] array, int[] coord) {
    if (!hasAbove(array, coord)) {
      throw new IllegalArgumentException("boolean coordinates do not have an element above");
    }
    if (verbose) {
      System.out.println(array[coord[0] - 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] - 1][coord[1]];
  }

  /**
   * Returns the element immediately above the coordinates specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element above the given coordinates
   */
  public static char above(char[][] array, int[] coord) {
    if (!hasAbove(array, coord)) {
      return Character.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(array[coord[0] - 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] - 1][coord[1]];
  }

  /**
   * Returns the element immediately below the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element below the specified coordinates
   */
  public static Object below(Object[][] array, int[] coord) {
    if (!hasBelow(array, coord)) {
      return null;
    }
    if (verbose) {
      System.out.println(array[coord[0] + 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] + 1][coord[1]];
  }

  /**
   * Returns the element immediately below the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element below the specified coordinates
   */
  public static int below(int[][] array, int[] coord) {
    if (!hasBelow(array, coord)) {
      return Integer.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(array[coord[0] + 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] + 1][coord[1]];
  }

  /**
   * Returns the element immediately below the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element below the specified coordinates
   */
  public static double below(double[][] array, int[] coord) {
    if (!hasBelow(array, coord)) {
      return Double.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(array[coord[0] + 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] + 1][coord[1]];
  }

  /**
   * Returns the element immediately below the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element below the specified coordinates
   */
  public static boolean below(boolean[][] array, int[] coord) {
    if (!hasBelow(array, coord)) {
      throw new IllegalArgumentException("boolean coordinates do not have an element below");
    }
    if (verbose) {
      System.out.println(array[coord[0] + 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] + 1][coord[1]];
  }

  /**
   * Returns the element immediately below the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element below the specified coordinates
   */
  public static char below(char[][] array, int[] coord) {
    if (!hasBelow(array, coord)) {
      return Character.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(array[coord[0] + 1][coord[1]] + " is above " + array[coord[0]][coord[1]]);
    }
    return array[coord[0] + 1][coord[1]];
  }

  /**
   * Returns the element immediately to the right of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the right of the coordinates
   */
  public static Object rightOf(Object[][] array, int[] coord) {
    if (!hasRight(array, coord)) {
      return null;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] + 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] + 1];
  }

  /**
   * Returns the element immediately to the right of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the right of the coordinates
   */
  public static int rightOf(int[][] array, int[] coord) {
    if (!hasRight(array, coord)) {
      return Integer.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] + 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] + 1];
  }

  /**
   * Returns the element immediately to the right of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the right of the coordinates
   */
  public static double rightOf(double[][] array, int[] coord) {
    if (!hasRight(array, coord)) {
      return Double.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] + 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] + 1];
  }

  /**
   * Returns the element immediately to the right of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the right of the coordinates
   */
  public static boolean rightOf(boolean[][] array, int[] coord) {
    if (!hasRight(array, coord)) {
      throw new IllegalArgumentException(
          "boolean coordinates do not have an element right of them");
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] + 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] + 1];
  }

  /**
   * Returns the element immediately to the right of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the right of the coordinates
   */
  public static char rightOf(char[][] array, int[] coord) {
    if (!hasRight(array, coord)) {
      return Character.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] + 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] + 1];
  }

  /**
   * Returns the element immediately to the left of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the left of the coordinates
   */
  public static Object leftOf(Object[][] array, int[] coord) {
    if (!hasLeft(array, coord)) {
      return null;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] - 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] - 1];
  }

  /**
   * Returns the element immediately to the left of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the left of the coordinates
   */
  public static int leftOf(int[][] array, int[] coord) {
    if (!hasLeft(array, coord)) {
      return Integer.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] - 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] - 1];
  }

  /**
   * Returns the element immediately to the left of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the left of the coordinates
   */
  public static double leftOf(double[][] array, int[] coord) {
    if (!hasLeft(array, coord)) {
      return Double.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] - 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] - 1];
  }

  /**
   * Returns the element immediately to the left of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the left of the coordinates
   */
  public static boolean leftOf(boolean[][] array, int[] coord) {
    if (!hasLeft(array, coord)) {
      throw new IllegalArgumentException("boolean coordinates do not have an element to the left");
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] - 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] - 1];
  }

  /**
   * Returns the element immediately to the left of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element to the left of the coordinates
   */
  public static char leftOf(char[][] array, int[] coord) {
    if (!hasLeft(array, coord)) {
      return Character.MAX_VALUE;
    }
    if (verbose) {
      System.out.println(
          array[coord[0]][coord[1] - 1] + " is to the right of " + array[coord[0]][coord[1]]);
    }
    return array[coord[0]][coord[1] - 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static Object nwOf(Object[][] array, int[] coord) {
    if (!hasNw(array, coord))
      return null;
    return array[coord[0] - 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static int nwOf(int[][] array, int[] coord) {
    if (!hasNw(array, coord))
      return Integer.MAX_VALUE;
    return array[coord[0] - 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static double nwOf(double[][] array, int[] coord) {
    if (!hasNw(array, coord))
      return Double.MAX_VALUE;
    return array[coord[0] - 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static boolean nwOf(boolean[][] array, int[] coord) {
    if (!hasNw(array, coord))
      throw new IllegalArgumentException("boolean coordinates do not have an element below");
    return array[coord[0] - 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static char nwOf(char[][] array, int[] coord) {
    if (!hasNw(array, coord))
      return Character.MAX_VALUE;
    return array[coord[0] - 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static Object neOf(Object[][] array, int[] coord) {
    if (!hasNe(array, coord))
      return null;
    return array[coord[0] - 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static int neOf(int[][] array, int[] coord) {
    if (!hasNe(array, coord))
      return Integer.MAX_VALUE;
    return array[coord[0] - 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static double neOf(double[][] array, int[] coord) {
    if (!hasNe(array, coord))
      return Double.MAX_VALUE;
    return array[coord[0] - 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static boolean neOf(boolean[][] array, int[] coord) {
    if (!hasNe(array, coord))
      throw new IllegalArgumentException("boolean coordinates do not have an element below");
    return array[coord[0] - 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately northwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element northwest of the coordinates
   */
  public static char neOf(char[][] array, int[] coord) {
    if (!hasNe(array, coord))
      return Character.MAX_VALUE;
    return array[coord[0] - 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately southeast of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southeast of the coordinates
   */
  public static Object seOf(Object[][] array, int[] coord) {
    if (!hasSe(array, coord))
      return null;
    return array[coord[0] + 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately southeast of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southeast of the coordinates
   */
  public static int seOf(int[][] array, int[] coord) {
    if (!hasSe(array, coord))
      return Integer.MAX_VALUE;
    return array[coord[0] + 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately southeast of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southeast of the coordinates
   */
  public static double seOf(double[][] array, int[] coord) {
    if (!hasSe(array, coord))
      return Double.MAX_VALUE;
    return array[coord[0] + 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately southeast of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southeast of the coordinates
   */
  public static boolean seOf(boolean[][] array, int[] coord) {
    if (!hasSe(array, coord))
      throw new IllegalArgumentException("boolean coordinates do not have an element below");
    return array[coord[0] + 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately southeast of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southeast of the coordinates
   */
  public static char seOf(char[][] array, int[] coord) {
    if (!hasSe(array, coord))
      return Character.MAX_VALUE;
    return array[coord[0] + 1][coord[1] + 1];
  }

  /**
   * Returns the element immediately southwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southwest of the coordinates
   */
  public static Object swOf(Object[][] array, int[] coord) {
    if (!hasSw(array, coord))
      return null;
    return array[coord[0] + 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately southwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southwest of the coordinates
   */
  public static int swOf(int[][] array, int[] coord) {
    if (!hasSw(array, coord))
      return Integer.MAX_VALUE;
    return array[coord[0] + 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately southwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southwest of the coordinates
   */
  public static double swOf(double[][] array, int[] coord) {
    if (!hasSw(array, coord))
      return Double.MAX_VALUE;
    return array[coord[0] + 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately southwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southwest of the coordinates
   */
  public static boolean swOf(boolean[][] array, int[] coord) {
    if (!hasSw(array, coord))
      throw new IllegalArgumentException("boolean coordinates do not have an element below");
    return array[coord[0] + 1][coord[1] - 1];
  }

  /**
   * Returns the element immediately southwest of the one specified in a 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates to use
   * @return the element southwest of the coordinates
   */
  public static char swOf(char[][] array, int[] coord) {
    if (!hasSw(array, coord))
      return Character.MAX_VALUE;
    return array[coord[0] + 1][coord[1] - 1];
  }

  /**
   * Returns an array of elements surrounding the specified coordinates in a 2D array.
   * 
   * @return the adjacent elements
   */
  public static Object[] adjacent(Object[][] array, int[] coord) {
    Object[] immediate = new Object[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = above(array, coord);
    immediate[2] = neOf(array, coord);
    immediate[3] = rightOf(array, coord);
    immediate[4] = seOf(array, coord);
    immediate[5] = below(array, coord);
    immediate[6] = swOf(array, coord);
    immediate[7] = leftOf(array, coord);
    ArrayList<Object> adj = new ArrayList<>();
    for (Object obj : immediate)
      if (obj != null)
        adj.add(obj);
    return adj.toArray();
  }

  /**
   * Returns an array of elements surrounding the specified coordinates in a 2D array.
   * 
   * @return the adjacent elements
   */
  public static int[] adjacent(int[][] array, int[] coord) {
    int[] immediate = new int[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = above(array, coord);
    immediate[2] = neOf(array, coord);
    immediate[3] = rightOf(array, coord);
    immediate[4] = seOf(array, coord);
    immediate[5] = below(array, coord);
    immediate[6] = swOf(array, coord);
    immediate[7] = leftOf(array, coord);
    ArrayList<Integer> adj = new ArrayList<>();
    for (int obj : immediate) {
      if (!isConstant(obj)) {
        adj.add(obj);
      }
    }
    return toInt(adj.toArray());
  }

  /**
   * Returns an array of elements surrounding the specified coordinates in a 2D array.
   * 
   * @return the adjacent elements
   */
  public static double[] adjacent(double[][] array, int[] coord) {
    double[] immediate = new double[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = above(array, coord);
    immediate[2] = neOf(array, coord);
    immediate[3] = rightOf(array, coord);
    immediate[4] = seOf(array, coord);
    immediate[5] = below(array, coord);
    immediate[6] = swOf(array, coord);
    immediate[7] = leftOf(array, coord);
    ArrayList<Double> adj = new ArrayList<>();
    for (double obj : immediate) {
      if (!isConstant(obj)) {
        adj.add(obj);
      }
    }
    return toDouble(adj.toArray());
  }

  /**
   * Returns an array of elements surrounding the specified coordinates in a 2D array.
   * 
   * @return the adjacent elements
   */
  public static boolean[] adjacent(boolean[][] array, int[] coord) {
    ArrayList<Boolean> immediate = new ArrayList<>();
    try {
      immediate.add(nwOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(above(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(neOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(rightOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(seOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(below(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(swOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(leftOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    ArrayList<Boolean> adj = new ArrayList<>();
    for (boolean obj : immediate) {
      adj.add(obj);
    }
    return toBoolean(adj.toArray());
  }

  /**
   * Returns an array of elements surrounding the specified coordinates in a 2D array.
   * 
   * @return the adjacent elements
   */
  public static char[] adjacent(char[][] array, int[] coord) {
    char[] immediate = new char[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = above(array, coord);
    immediate[2] = neOf(array, coord);
    immediate[3] = rightOf(array, coord);
    immediate[4] = seOf(array, coord);
    immediate[5] = below(array, coord);
    immediate[6] = swOf(array, coord);
    immediate[7] = leftOf(array, coord);
    ArrayList<Character> adj = new ArrayList<>();
    for (char obj : immediate) {
      if (!isConstant(obj)) {
        adj.add(obj);
      }
    }
    return toChar(adj.toArray());
  }

  /**
   * Gets the elements in each immediate cardinal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the cardinal elements
   */
  public static Object[] cardinal(Object[][] array, int[] coord) {
    Object[] immediate = new Object[4];
    immediate[0] = above(array, coord);
    immediate[1] = rightOf(array, coord);
    immediate[2] = below(array, coord);
    immediate[3] = leftOf(array, coord);
    ArrayList<Object> adj = new ArrayList<>();
    for (Object obj : immediate)
      if (obj != null)
        adj.add(obj);
    return adj.toArray();
  }

  /**
   * Gets the elements in each immediate cardinal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the cardinal elements
   */
  public static int[] cardinal(int[][] array, int[] coord) {
    int[] immediate = new int[4];
    immediate[0] = above(array, coord);
    immediate[1] = rightOf(array, coord);
    immediate[2] = below(array, coord);
    immediate[3] = leftOf(array, coord);
    ArrayList<Integer> adj = new ArrayList<>();
    for (int obj : immediate)
      if (!isConstant(obj))
        adj.add(obj);
    return toInt(adj.toArray());
  }

  /**
   * Gets the elements in each immediate cardinal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the cardinal elements
   */
  public static double[] cardinal(double[][] array, int[] coord) {
    double[] immediate = new double[4];
    immediate[0] = above(array, coord);
    immediate[1] = rightOf(array, coord);
    immediate[2] = below(array, coord);
    immediate[3] = leftOf(array, coord);
    ArrayList<Double> adj = new ArrayList<>();
    for (double obj : immediate)
      if (!isConstant(obj))
        adj.add(obj);
    return toDouble(adj.toArray());
  }

  /**
   * Gets the elements in each immediate cardinal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the cardinal elements
   */
  public static boolean[] cardinal(boolean[][] array, int[] coord) {
    ArrayList<Boolean> immediate = new ArrayList<>();
    try {
      immediate.add(above(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(rightOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(below(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(leftOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    ArrayList<Boolean> adj = new ArrayList<>();
    for (boolean obj : immediate)
      adj.add(obj);
    return toBoolean(adj.toArray());
  }

  /**
   * Gets the elements in each immediate cardinal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the cardinal elements
   */
  public static char[] cardinal(char[][] array, int[] coord) {
    char[] immediate = new char[4];
    immediate[0] = above(array, coord);
    immediate[1] = rightOf(array, coord);
    immediate[2] = below(array, coord);
    immediate[3] = leftOf(array, coord);
    ArrayList<Character> adj = new ArrayList<>();
    for (char obj : immediate)
      if (!isConstant(obj))
        adj.add(obj);
    return toChar(adj.toArray());
  }

  /**
   * Gets the elements in each immediate diagonal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the diagonal elements
   */
  public static Object[] diagonal(Object[][] array, int[] coord) {
    Object[] immediate = new Object[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = neOf(array, coord);
    immediate[2] = seOf(array, coord);
    immediate[3] = swOf(array, coord);
    ArrayList<Object> adj = new ArrayList<>();
    for (Object obj : immediate)
      if (obj != null)
        adj.add(obj);
    return adj.toArray();
  }

  /**
   * Gets the elements in each immediate diagonal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the diagonal elements
   */
  public static int[] diagonal(int[][] array, int[] coord) {
    int[] immediate = new int[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = neOf(array, coord);
    immediate[2] = seOf(array, coord);
    immediate[3] = swOf(array, coord);
    ArrayList<Integer> adj = new ArrayList<>();
    for (int obj : immediate)
      if (!isConstant(obj))
        adj.add(obj);
    return toInt(adj.toArray());
  }

  /**
   * Gets the elements in each immediate diagonal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the diagonal elements
   */
  public static double[] diagonal(double[][] array, int[] coord) {
    double[] immediate = new double[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = neOf(array, coord);
    immediate[2] = seOf(array, coord);
    immediate[3] = swOf(array, coord);
    ArrayList<Double> adj = new ArrayList<>();
    for (double obj : immediate)
      if (!isConstant(obj))
        adj.add(obj);
    return toDouble(adj.toArray());
  }

  /**
   * Gets the elements in each immediate diagonal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the diagonal elements
   */
  public static boolean[] diagonal(boolean[][] array, int[] coord) {
    ArrayList<Boolean> immediate = new ArrayList<>();
    try {
      immediate.add(nwOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(neOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(seOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    try {
      immediate.add(swOf(array, coord));
    } catch (IllegalArgumentException e) {
    }
    ArrayList<Boolean> adj = new ArrayList<>();
    for (boolean obj : immediate)
      adj.add(obj);
    return toBoolean(adj.toArray());
  }

  /**
   * Gets the elements in each immediate diagonal direction in the 2D array.
   * 
   * @param array the array to use
   * @param coord the coordinates
   * @return the diagonal elements
   */
  public static char[] diagonal(char[][] array, int[] coord) {
    char[] immediate = new char[8];
    immediate[0] = nwOf(array, coord);
    immediate[1] = neOf(array, coord);
    immediate[2] = seOf(array, coord);
    immediate[3] = swOf(array, coord);
    ArrayList<Character> adj = new ArrayList<>();
    for (char obj : immediate)
      if (!isConstant(obj))
        adj.add(obj);
    return toChar(adj.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static Object[] aboveAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    for (int i = 1; i < coord[0] + 1; i++)
      list.add(directionalMulti(array, coord, "north", i, true));
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static int[] aboveAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 1; i < coord[0] + 1; i++)
      list.add(directionalMulti(array, coord, "north", i, true));
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static double[] aboveAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    for (int i = 1; i < coord[0] + 1; i++)
      list.add(directionalMulti(array, coord, "north", i, true));
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static boolean[] aboveAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 1; i < coord[0] + 1; i++)
      list.add(directionalMulti(array, coord, "north", i, true));
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static char[] aboveAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    for (int i = 1; i < coord[0] + 1; i++)
      list.add(directionalMulti(array, coord, "north", i, true));
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static Object[] belowAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    for (int i = 1; i < array.length - coord[0]; i++)
      list.add(directionalMulti(array, coord, "south", i, true));
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static int[] belowAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 1; i < array.length - coord[0]; i++)
      list.add(directionalMulti(array, coord, "south", i, true));
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static double[] belowAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    for (int i = 1; i < array.length - coord[0]; i++)
      list.add(directionalMulti(array, coord, "south", i, true));
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static boolean[] belowAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 1; i < array.length - coord[0]; i++)
      list.add(directionalMulti(array, coord, "south", i, true));
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static char[] belowAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    for (int i = 1; i < array.length - coord[0]; i++)
      list.add(directionalMulti(array, coord, "south", i, true));
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static Object[] rightAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    for (int i = 1; i < array[coord[0]].length - coord[1]; i++)
      list.add(directionalMulti(array, coord, "east", i, true));
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static int[] rightAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 1; i < array[coord[0]].length - coord[1]; i++)
      list.add(directionalMulti(array, coord, "east", i, true));
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static double[] rightAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    for (int i = 1; i < array[coord[0]].length - coord[1]; i++)
      list.add(directionalMulti(array, coord, "east", i, true));
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static boolean[] rightAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 1; i < array[coord[0]].length - coord[1]; i++)
      list.add(directionalMulti(array, coord, "east", i, true));
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static char[] rightAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    for (int i = 1; i < array[coord[0]].length - coord[1]; i++)
      list.add(directionalMulti(array, coord, "east", i, true));
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static Object[] leftAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    for (int i = 1; i < coord[1] + 1; i++)
      list.add(directionalMulti(array, coord, "west", i, true));
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static int[] leftAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 1; i < coord[1] + 1; i++)
      list.add(directionalMulti(array, coord, "west", i, true));
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static double[] leftAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    for (int i = 1; i < coord[1] + 1; i++)
      list.add(directionalMulti(array, coord, "west", i, true));
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static boolean[] leftAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 1; i < coord[1] + 1; i++)
      list.add(directionalMulti(array, coord, "west", i, true));
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly above the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly above coord
   * @since 0.1.5.2.8
   */
  public static char[] leftAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    for (int i = 1; i < coord[1] + 1; i++)
      list.add(directionalMulti(array, coord, "west", i, true));
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly northeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northeast of coord
   * @since 0.1.5.3.4
   */
  public static Object[] neAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    int i = 1;
    Object curr = directionalMulti(array, coord, "ne", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "ne", i, false);
    }
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly northeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northeast of coord
   * @since 0.1.5.3.4
   */
  public static int[] neAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    int i = 1;
    int curr = directionalMulti(array, coord, "ne", i, false);
    while (curr != Integer.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "ne", i, false);
    }
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly northeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northeast of coord
   * @since 0.1.5.3.4
   */
  public static double[] neAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    int i = 1;
    double curr = directionalMulti(array, coord, "ne", i, false);
    while (curr != Double.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "ne", i, false);
    }
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly northeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northeast of coord
   * @since 0.1.5.3.4
   */
  public static boolean[] neAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    int i = 1;
    Boolean curr = directionalMulti(array, coord, "ne", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "ne", i, false);
    }
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly northeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northeast of coord
   * @since 0.1.5.3.4
   */
  public static char[] neAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    int i = 1;
    char curr = directionalMulti(array, coord, "ne", i, false);
    while (curr != '?') {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "ne", i, false);
    }
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly northeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northeast of coord
   * @since 0.1.5.3.4
   */
  public static Object[] seAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    int i = 1;
    Object curr = directionalMulti(array, coord, "se", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "se", i, false);
    }
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly southeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southeast of coord
   * @since 0.1.5.3.4
   */
  public static int[] seAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    int i = 1;
    int curr = directionalMulti(array, coord, "se", i, false);
    while (curr != Integer.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "se", i, false);
    }
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly southeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southeast of coord
   * @since 0.1.5.3.4
   */
  public static double[] seAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    int i = 1;
    double curr = directionalMulti(array, coord, "se", i, false);
    while (curr != Double.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "se", i, false);
    }
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly southeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southeast of coord
   * @since 0.1.5.3.4
   */
  public static boolean[] seAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    int i = 1;
    Boolean curr = directionalMulti(array, coord, "se", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "se", i, false);
    }
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly southeast of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southeast of coord
   * @since 0.1.5.3.4
   */
  public static char[] seAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    int i = 1;
    char curr = directionalMulti(array, coord, "se", i, false);
    while (curr != '?') {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "se", i, false);
    }
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly southwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southwest of coord
   * @since 0.1.5.3.4
   */
  public static Object[] swAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    int i = 1;
    Object curr = directionalMulti(array, coord, "sw", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "sw", i, false);
    }
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly southwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southwest of coord
   * @since 0.1.5.3.4
   */
  public static int[] swAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    int i = 1;
    int curr = directionalMulti(array, coord, "sw", i, false);
    while (curr != Integer.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "sw", i, false);
    }
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly southwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southwest of coord
   * @since 0.1.5.3.4
   */
  public static double[] swAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    int i = 1;
    double curr = directionalMulti(array, coord, "sw", i, false);
    while (curr != Double.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "sw", i, false);
    }
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly southwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southwest of coord
   * @since 0.1.5.3.4
   */
  public static boolean[] swAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    int i = 1;
    Boolean curr = directionalMulti(array, coord, "sw", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "sw", i, false);
    }
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly southwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly southwest of coord
   * @since 0.1.5.3.4
   */
  public static char[] swAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    int i = 1;
    char curr = directionalMulti(array, coord, "sw", i, false);
    while (curr != '?') {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "sw", i, false);
    }
    return toChar(list.toArray());
  }

  /**
   * Gets an array of all elements directly northwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northwest of coord
   * @since 0.1.5.3.4
   */
  public static Object[] nwAll(Object[][] array, int[] coord) {
    ArrayList<Object> list = new ArrayList<>();
    int i = 1;
    Object curr = directionalMulti(array, coord, "nw", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "nw", i, false);
    }
    return list.toArray();
  }

  /**
   * Gets an array of all elements directly northwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northwest of coord
   * @since 0.1.5.3.4
   */
  public static int[] nwAll(int[][] array, int[] coord) {
    ArrayList<Integer> list = new ArrayList<>();
    int i = 1;
    int curr = directionalMulti(array, coord, "nw", i, false);
    while (curr != Integer.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "nw", i, false);
    }
    return toInt(list.toArray());
  }

  /**
   * Gets an array of all elements directly northwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northwest of coord
   * @since 0.1.5.3.4
   */
  public static double[] nwAll(double[][] array, int[] coord) {
    ArrayList<Double> list = new ArrayList<>();
    int i = 1;
    double curr = directionalMulti(array, coord, "nw", i, false);
    while (curr != Double.MAX_VALUE) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "nw", i, false);
    }
    return toDouble(list.toArray());
  }

  /**
   * Gets an array of all elements directly northwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northwest of coord
   * @since 0.1.5.3.4
   */
  public static boolean[] nwAll(boolean[][] array, int[] coord) {
    ArrayList<Boolean> list = new ArrayList<>();
    int i = 1;
    Boolean curr = directionalMulti(array, coord, "nw", i, false);
    while (curr != null) {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "nw", i, false);
    }
    return toBoolean(list.toArray());
  }

  /**
   * Gets an array of all elements directly northwest of the coordinates specified.
   * 
   * @param array the array
   * @param coord the coordinates
   * @return the elements directly northwest of coord
   * @since 0.1.5.3.4
   */
  public static char[] nwAll(char[][] array, int[] coord) {
    ArrayList<Character> list = new ArrayList<>();
    int i = 1;
    char curr = directionalMulti(array, coord, "nw", i, false);
    while (curr != '?') {
      list.add(curr);
      i++;
      curr = directionalMulti(array, coord, "nw", i, false);
    }
    return toChar(list.toArray());
  }

  /**
   * Gets the points above the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsAbove(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "north", i, false);
    while (curr.getX() >= 0) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "north", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points below the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsBelow(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "south", i, false);
    while (curr.getX() < array.length) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "south", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points right of the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsRight(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "east", i, false);
    while (curr.getY() < array[(int) curr.getX()].length) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "east", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points left of the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsLeft(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "west", i, false);
    while (curr.getY() >= 0) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "west", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points northeast of the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsNe(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "ne", i, false);
    while (curr.getX() >= 0 && curr.getY() < array[(int) curr.getX()].length) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "ne", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points northeast of the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsSe(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "se", i, false);
    while (curr.getX() < array.length && curr.getY() < array[(int) curr.getX()].length) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "se", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points northeast of the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsSw(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "sw", i, false);
    while (curr.getX() < array.length && curr.getY() >= 0) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "sw", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the points northeast of the coordinates specified in the array passed.
   * 
   * @param array the array
   * @param coord the coords
   * @return the Points that exist
   * @since 0.1.5.3.4
   */
  public static Point[] pointsNw(Object[][] array, int[] coord) {
    ArrayList<Point> points = new ArrayList<>();
    int i = 1;
    Point curr = pointMulti(array, coord, "nw", i, false);
    while (curr.getX() >= 0 && curr.getY() >= 0) {
      points.add(curr);
      i++;
      curr = pointMulti(array, coord, "nw", i, false);
    }
    return points.toArray(Point[]::new);
  }

  /**
   * Gets the Point thats the specified distance away from the specified center the the direction of
   * the one specified.
   * 
   * @param array the array
   * @param center the center
   * @param direction the direction
   * @param distance the distance
   * @param includeEdges whether to include edges or not
   * @return the Point
   * @since 0.1.5.3.4
   */
  public static Point pointMulti(Object[][] array, int[] from, String direction, int distance,
      boolean includeEdges) {
    Point obj = null;
    String[] allowed = {"north", "south", "east", "west", "nw", "ne", "sw", "se"};
    if (contains(allowed, direction)) {
      if (direction.equals("north")) {
        try {
          obj = new Point(from[0] - distance, from[1]);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1]})) {
              iterator--;
            }
            obj = new Point(from[0] - iterator, from[1]);
          }
        }
      } else if (direction.equals("south")) {
        try {
          obj = new Point(from[0] + distance, from[1]);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1]})) {
              iterator--;
            }
            obj = new Point(from[0] + iterator, from[1]);
          }
        }
      } else if (direction.equals("east")) {
        try {
          obj = new Point(from[0], from[1] + distance);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] + iterator})) {
              iterator--;
            }
            obj = new Point(from[0], from[1] + iterator);
          }
        }
      } else if (direction.equals("west")) {
        try {
          obj = new Point(from[0], from[1] - distance);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] - iterator})) {
              iterator--;
            }
            obj = new Point(from[0], from[1] - iterator);
          }
        }
      } else if (direction.equals("nw")) {
        try {
          obj = new Point(from[0] - distance, from[1] - distance);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = new Point(from[0] - iterator, from[1] - iterator);
          }
        }
      } else if (direction.equals("ne")) {
        try {
          obj = new Point(from[0] - distance, from[1] + distance);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = new Point(from[0] - iterator, from[1] + iterator);
          }
        }
      } else if (direction.equals("sw")) {
        try {
          obj = new Point(from[0] + distance, from[1] - distance);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = new Point(from[0] + iterator, from[1] - iterator);
          }
        }
      } else if (direction.equals("se")) {
        try {
          obj = new Point(from[0] + distance, from[1] + distance);
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = new Point(from[0] + iterator, from[1] + iterator);
          }
        }
      }
    }
    return obj;
  }

  /**
   * Generates a distance table for the coordinates provided.
   * 
   * @param <T> Generic
   * @param array the array to reference
   * @param from the from coordinates
   * @return
   */
  public static <T> double[][] generateDistanceInvTable(T[][] array, int[] from) {
    double[][] dcopy = create2DDoubleArrayFromObj(array);
    for (int i = 0; i < dcopy.length; i++)
      for (int j = 0; j < dcopy[i].length; j++)
        dcopy[i][j] = distanceInv(dcopy, from, new int[] {i, j});
    return dcopy;
  }

  /**
   * Creates a map with distances from a specific coordinate in the array specified.
   * 
   * @param dt the premade distance table
   * @param array the array
   * @param center the center
   * @return a distance map
   */
  public static embMsnMultimap<Double, Point> distanceMap(double[][] dt, Object[][] array,
      int[] center) {
    embMsnMultimap<Double, Point> map = new embMsnMultimap<>();
    for (int i = 0; i < dt.length; i++)
      for (int j = 0; j < dt[i].length; j++)
        map.put(dt[i][j], new Point(i, j));
    return map;
  }

  /**
   * Creates a map with distances from a specific coordinate in the array specified.
   * 
   * @param <T> Generic
   * 
   * @param array the array
   * @param center the center
   * @return a distance map
   */
  public static <T> embMsnMultimap<Double, Point> distanceMap(T[][] array, int[] center) {
    double[][] dt = generateDistanceInvTable(array, center);
    embMsnMultimap<Double, Point> map = new embMsnMultimap<>();
    for (int i = 0; i < dt.length; i++)
      for (int j = 0; j < dt[i].length; j++)
        map.put(dt[i][j], new Point(i, j));
    return map;
  }

  /**
   * Obtains the elements with the same radius from the specified when given a radius.
   * 
   * @param <T> Generic
   * 
   * @param radius the radius of the circle
   * @param array the array
   * @param center the coordinates of the center of the circle
   * @return the elements forming a circle
   */
  public static <T> ArrayList<T> circular(T[][] array, int radius, int[] center) {
    ArrayList<T> circle = new ArrayList<>();
    double[][] dt = generateDistanceInvTable(array, center);
    embMsnMultimap<Double, Point> distanceMap = distanceMap(dt, array, center);
    double startdistance = directionalMulti(dt, center, "north", radius, true);
    while (isConstant(startdistance)) {
      startdistance = directionalMulti(dt, center, "south", radius, true);
      if (!isConstant(startdistance))
        break;
      startdistance = directionalMulti(dt, center, "east", radius, true);
      if (!isConstant(startdistance))
        break;
      startdistance = directionalMulti(dt, center, "west", radius, true);
      if (!isConstant(startdistance))
        break;
    }
    double currdist = startdistance;
    ArrayList<Point> points = distanceMap.get(currdist);
    for (Point p : points)
      circle.add(array[(int) p.getX()][(int) p.getY()]);
    while (circle.size() < Math.ceil(2 * Math.PI * radius)) {
      ArrayList<Double> adjdistance = new ArrayList<>();
      for (Point init : points)
        adjdistance = new ArrayList<>(
            Arrays.asList(box(adjacent(dt, new int[] {(int) init.getX(), (int) init.getY()}))));
      double[] adj = toDouble(adjdistance.toArray());
      currdist = closestTo(currdist, adj);
      points = distanceMap.get(currdist);
      for (Point p : points)
        circle.add(array[(int) p.getX()][(int) p.getY()]);
    }
    return circle;
  }

  /**
   * Determines whether an element is a member of directionalConstants.
   * 
   * @param i the obj to check
   */
  public static boolean isConstant(int i) {
    return i == intDirectionalConstant;
  }

  /**
   * Determines whether an element is a member of directionalConstants.
   * 
   * @param i the obj to check
   */
  public static boolean isConstant(double d) {
    return d == doubleDirectionalConstant;
  }

  /**
   * Determines whether an element is a member of directionalConstants.
   * 
   * @param i the obj to check
   */
  public static boolean isConstant(char c) {
    return c == charDirectionalConstant;
  }

  /**
   * Gets an element of the array a certain amount of blocks away.
   * 
   * @param <T> Generic
   * 
   * @param array the array to use
   * @param from the initial coordinates
   * @param direction the direction {"north", "south", "east", "west", "nw", "ne", "sw", "se"}
   * @param distance the distance (not to be confused with distance() methods)
   * @param includeEdges whether to return an edge if the distance is out of bounds
   * @return the Object
   */
  public static <T> T directionalMulti(T[][] array, int[] from, String direction, int distance,
      boolean includeEdges) {
    T obj = null;
    String[] allowed = {"north", "south", "east", "west", "nw", "ne", "sw", "se"};
    if (contains(allowed, direction)) {
      if (direction.equals("north")) {
        try {
          obj = array[from[0] - distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1]];
          }
        }
      } else if (direction.equals("south")) {
        try {
          obj = array[from[0] + distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1]];
          }
        }
      } else if (direction.equals("east")) {
        try {
          obj = array[from[0]][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] + iterator];
          }
        }
      } else if (direction.equals("west")) {
        try {
          obj = array[from[0]][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] - iterator];
          }
        }
      } else if (direction.equals("nw")) {
        try {
          obj = array[from[0] - distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("ne")) {
        try {
          obj = array[from[0] - distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] + iterator];
          }
        }
      } else if (direction.equals("sw")) {
        try {
          obj = array[from[0] + distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("se")) {
        try {
          obj = array[from[0] + distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] + iterator];
          }
        }
      }
    }
    return obj;
  }

  /**
   * Gets an element of the array a certain amount of blocks away.
   * 
   * @param array the array to use
   * @param from the initial coordinates
   * @param direction the direction {"north", "south", "east", "west", "nw", "ne", "sw", "se"}
   * @param distance the distance (not to be confused with distance() methods)
   * @param includeEdges whether to return an edge if the distance is out of bounds
   * @return the Object
   */
  public static int directionalMulti(int[][] array, int[] from, String direction, int distance,
      boolean includeEdges) {
    int obj = Integer.MAX_VALUE;
    String[] allowed = {"north", "south", "east", "west", "nw", "ne", "sw", "se"};
    if (contains(allowed, direction)) {
      if (direction.equals("north")) {
        try {
          obj = array[from[0] - distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1]];
          }
        }
      } else if (direction.equals("south")) {
        try {
          obj = array[from[0] + distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1]];
          }
        }
      } else if (direction.equals("east")) {
        try {
          obj = array[from[0]][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] + iterator];
          }
        }
      } else if (direction.equals("west")) {
        try {
          obj = array[from[0]][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] - iterator];
          }
        }
      } else if (direction.equals("nw")) {
        try {
          obj = array[from[0] - distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("ne")) {
        try {
          obj = array[from[0] - distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] + iterator];
          }
        }
      } else if (direction.equals("sw")) {
        try {
          obj = array[from[0] + distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("se")) {
        try {
          obj = array[from[0] + distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] + iterator];
          }
        }
      }
    }
    return obj;
  }

  /**
   * Gets an element of the array a certain amount of blocks away.
   * 
   * @param array the array to use
   * @param from the initial coordinates
   * @param direction the direction {"north", "south", "east", "west", "nw", "ne", "sw", "se"}
   * @param distance the distance (not to be confused with distance() methods)
   * @param includeEdges whether to return an edge if the distance is out of bounds
   * @return the Object
   */
  public static double directionalMulti(double[][] array, int[] from, String direction,
      int distance, boolean includeEdges) {
    double obj = Double.MAX_VALUE;
    String[] allowed = {"north", "south", "east", "west", "nw", "ne", "sw", "se"};
    if (contains(allowed, direction)) {
      if (direction.equals("north")) {
        try {
          obj = array[from[0] - distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1]];
          }
        }
      } else if (direction.equals("south")) {
        try {
          obj = array[from[0] + distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1]];
          }
        }
      } else if (direction.equals("east")) {
        try {
          obj = array[from[0]][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] + iterator];
          }
        }
      } else if (direction.equals("west")) {
        try {
          obj = array[from[0]][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] - iterator];
          }
        }
      } else if (direction.equals("nw")) {
        try {
          obj = array[from[0] - distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("ne")) {
        try {
          obj = array[from[0] - distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] + iterator];
          }
        }
      } else if (direction.equals("sw")) {
        try {
          obj = array[from[0] + distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("se")) {
        try {
          obj = array[from[0] + distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] + iterator];
          }
        }
      }
    }
    return obj;
  }

  /**
   * Gets an element of the array a certain amount of blocks away.
   * 
   * @param array the array to use
   * @param from the initial coordinates
   * @param direction the direction {"north", "south", "east", "west", "nw", "ne", "sw", "se"}
   * @param distance the distance (not to be confused with distance() methods)
   * @param includeEdges whether to return an edge if the distance is out of bounds
   * @return the Object
   */
  public static boolean directionalMulti(boolean[][] array, int[] from, String direction,
      int distance, boolean includeEdges) {
    Boolean obj = null;
    String[] allowed = {"north", "south", "east", "west", "nw", "ne", "sw", "se"};
    if (contains(allowed, direction)) {
      if (direction.equals("north")) {
        try {
          obj = array[from[0] - distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1]];
          }
        }
      } else if (direction.equals("south")) {
        try {
          obj = array[from[0] + distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1]];
          }
        }
      } else if (direction.equals("east")) {
        try {
          obj = array[from[0]][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] + iterator];
          }
        }
      } else if (direction.equals("west")) {
        try {
          obj = array[from[0]][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] - iterator];
          }
        }
      } else if (direction.equals("nw")) {
        try {
          obj = array[from[0] - distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("ne")) {
        try {
          obj = array[from[0] - distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] + iterator];
          }
        }
      } else if (direction.equals("sw")) {
        try {
          obj = array[from[0] + distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("se")) {
        try {
          obj = array[from[0] + distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] + iterator];
          }
        }
      }
    }
    return obj;
  }

  /**
   * Gets an element of the array a certain amount of blocks away.
   * 
   * @param array the array to use
   * @param from the initial coordinates
   * @param direction the direction {"north", "south", "east", "west", "nw", "ne", "sw", "se"}
   * @param distance the distance (not to be confused with distance() methods)
   * @param includeEdges whether to return an edge if the distance is out of bounds
   * @return the Object
   */
  public static char directionalMulti(char[][] array, int[] from, String direction, int distance,
      boolean includeEdges) {
    char obj = Character.MAX_VALUE;
    String[] allowed = {"north", "south", "east", "west", "nw", "ne", "sw", "se"};
    if (contains(allowed, direction)) {
      if (direction.equals("north")) {
        try {
          obj = array[from[0] - distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1]];
          }
        }
      } else if (direction.equals("south")) {
        try {
          obj = array[from[0] + distance][from[1]];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1]})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1]];
          }
        }
      } else if (direction.equals("east")) {
        try {
          obj = array[from[0]][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] + iterator];
          }
        }
      } else if (direction.equals("west")) {
        try {
          obj = array[from[0]][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0], from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0]][from[1] - iterator];
          }
        }
      } else if (direction.equals("nw")) {
        try {
          obj = array[from[0] - distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("ne")) {
        try {
          obj = array[from[0] - distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] - iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] - iterator][from[1] + iterator];
          }
        }
      } else if (direction.equals("sw")) {
        try {
          obj = array[from[0] + distance][from[1] - distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] - iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] - iterator];
          }
        }
      } else if (direction.equals("se")) {
        try {
          obj = array[from[0] + distance][from[1] + distance];
        } catch (IndexOutOfBoundsException e) {
          if (includeEdges) {
            int iterator = distance - 1;
            while (!validCoord(array, new int[] {from[0] + iterator, from[1] + iterator})) {
              iterator--;
            }
            obj = array[from[0] + iterator][from[1] + iterator];
          }
        }
      }
    }
    return obj;
  }

  // ----------STREAMS AND ITERATION----------------

  /**
   * Generates a Stream from the String specified.
   * 
   * @param s the String
   * @return the Stream
   * @since 0.1.5.2.5
   */
  public static Stream<Character> toStream(String s) {
    if (verbose)
      println("[*] converting to Stream");
    return s.chars().mapToObj(c -> (char) c);
  }

  /**
   * Iterates through the characters of the given String.
   * 
   * @param s the String
   * @return an Iterator
   * @since 0.1.5.2.7
   */
  public static Iterator<Character> iterator(String s) {
    return Arrays.asList(box(s.toCharArray())).iterator();
  }

  /**
   * Iterates through the elements of the given array.
   * 
   * @param <T> Generic
   * 
   * @param array the array
   * @return an Iterator
   * @since 0.1.5.2.7
   */
  public static <T> Iterator<T> iterator(T[] array) {
    return Arrays.asList(array).iterator();
  }

  /**
   * Iterates through the elements of the given array.
   * 
   * @param array the array
   * @return an Iterator
   * @since 0.1.5.2.7
   */
  public static Iterator<Integer> iterator(int[] array) {
    return Arrays.asList(box(array)).iterator();
  }

  /**
   * Iterates through the elements of the given array.
   * 
   * @param array the array
   * @return an Iterator
   * @since 0.1.5.2.7
   */
  public static Iterator<Double> iterator(double[] array) {
    return Arrays.asList(box(array)).iterator();
  }

  /**
   * Iterates through the elements of the given array.
   * 
   * @param array the array
   * @return an Iterator
   * @since 0.1.5.2.7
   */
  public static Iterator<Boolean> iterator(boolean[] array) {
    return Arrays.asList(box(array)).iterator();
  }

  /**
   * Iterates through the elements of the given array.
   * 
   * @param array the array
   * @return an Iterator
   * @since 0.1.5.2.7
   */
  public static Iterator<Character> iterator(char[] array) {
    return Arrays.asList(box(array)).iterator();
  }

  // --------------------------------MATH-------------------------------------

  /**
   * Reads a String as a math expression and evaluates.
   * 
   * Author: Boann on StackOverflow
   * 
   * @param str the String representation of the math expression to parse
   * @return the evaluation
   * @since 0.1.5.3.2
   */
  public static Object evaluate(String str) {
    return new Object() {
      int pos = -1, ch;

      void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
      }

      boolean eat(int charToEat) {
        while (ch == ' ')
          nextChar();
        if (ch == charToEat) {
          nextChar();
          return true;
        }
        return false;
      }

      double parse() {
        nextChar();
        double x = parseExpression();
        if (pos < str.length())
          throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
      }

      double parseExpression() {
        double x = parseTerm();
        for (;;) {
          if (eat('+'))
            x += parseTerm(); // addition
          else if (eat('-'))
            x -= parseTerm(); // subtraction
          else
            return x;
        }
      }

      double parseTerm() {
        double x = parseFactor();
        for (;;) {
          if (eat('*'))
            x *= parseFactor(); // multiplication
          else if (eat('/'))
            x /= parseFactor(); // division
          else
            return x;
        }
      }

      double parseFactor() {
        if (eat('+'))
          return parseFactor(); // unary plus
        if (eat('-'))
          return -parseFactor(); // unary minus
        double x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
          x = parseExpression();
          eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
          while ((ch >= '0' && ch <= '9') || ch == '.')
            nextChar();
          x = Double.parseDouble(str.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') { // functions
          while (ch >= 'a' && ch <= 'z')
            nextChar();
          String func = str.substring(startPos, this.pos);
          x = parseFactor();
          if (func.equals("sqrt"))
            x = Math.sqrt(x);
          else if (func.equals("sin"))
            x = Math.sin(Math.toRadians(x));
          else if (func.equals("cos"))
            x = Math.cos(Math.toRadians(x));
          else if (func.equals("tan"))
            x = Math.tan(Math.toRadians(x));
          else
            throw new RuntimeException("Unknown function: " + func);
        } else {
          throw new RuntimeException("Unexpected: " + (char) ch);
        }
        if (eat('^'))
          x = Math.pow(x, parseFactor()); // exponentiation
        return x;
      }
    }.parse();
  }

  /**
   * Checks whether the given number is even.
   * 
   * @param number the number to check
   * @return whether the number is even or not
   * @since 0.1.1.7.2
   */
  public static boolean isEven(int number) {
    return number % 2 == 0;
  }

  /**
   * Properly rounds a number.
   * 
   * @param number the number to round
   * @return the rounded number
   * @since 0.1.1.7.2
   */
  public static int round(double number) {
    int upper = (int) Math.ceil(number);
    int lower = (int) Math.floor(number);
    double upperdistance = upper - number;
    double lowerdistance = number - lower;
    if (upperdistance == lowerdistance)
      return upper;
    else if (upperdistance > lowerdistance)
      return lower;
    else
      return upper;
  }

  /**
   * Rounds a double to the nearest tenth.
   * 
   * @param number the number to round
   * @return the rounded number
   */
  public static double roundTenth(double number) {
    return (double) Math.round(number * 10) / 10;
  }

  /**
   * Calculates the average of a given array.
   * 
   * @param arr the array to use
   * @return the average of all entries of the array
   * @since 0.1.0.1.3
   */
  public static double avg(double[] arr) {
    return sum(arr) / arr.length;
  }

  /**
   * Calculates the average of a given array.
   * 
   * @param arr the array to use
   * @return the average of all entries of the array
   * @since 0.1.0.1.3
   */
  public static double avg(int[] arr) {
    return sum(arr) / arr.length;
  }

  /**
   * Checks whether a number divides another (|).
   * 
   * @param num1 the first number
   * @param num2 the second number
   * @return whether num1 divides num2
   * @since 0.1.0.1.3
   */
  public static boolean divides(double num1, double num2) {
    if (Math.floor(num2 / num1) == num2 / num1)
      return true;
    return false;
  }

  /**
   * Returns the sum of every value in an array.
   * 
   * @param array the array to use
   * @return the sum
   * @since 0.1.0.1.3
   */
  public static int sum(int[] array) {
    int sum = 0;
    for (int i = 0; i < array.length; i++)
      sum += array[i];
    return sum;
  }

  /**
   * Returns the sum of every value in an array.
   * 
   * @param array the array to use
   * @return the sum
   * @since 0.1.0.1.3
   */
  public static double sum(double[] array) {
    double sum = 0;
    for (int i = 0; i < array.length; i++)
      sum += array[i];
    return sum;
  }

  /**
   * Returns the greatest common divisor of the two integers passed.
   * 
   * @param num1 the first number
   * @param num2 the second number
   * @return the gcd
   * @since 0.1.0.1.3
   */
  public static int gcd(int num1, int num2) {
    while (num1 != num2)
      if (num1 > num2)
        num1 = num1 - num2;
      else
        num2 = num2 - num1;
    if (verbose)
      println("GCD of given numbers is: " + num2);
    return num2;
  }

  /**
   * Returns the greatest common divisor of the two integers passed.
   * 
   * @param num1 the first number
   * @param num2 the second number
   * @return the gcd
   * @since 0.1.0.1.3
   */
  public static double gcd(double num1, double num2) {
    while (num1 != num2)
      if (num1 > num2)
        num1 = num1 - num2;
      else
        num2 = num2 - num1;
    if (verbose)
      println("[+] GCD of given numbers is: " + num2);
    return num2;
  }

  /**
   * Returns the least common multiple of the two integers passed.
   * 
   * @param d the first number
   * @param e the second number
   * @return the lcm
   * @since 0.1.0.1.3
   */
  public static double lcm(double d, double e) {
    if (d == 0 || e == 0)
      return 0;
    double absNumber1 = Math.abs(d);
    double absNumber2 = Math.abs(e);
    double absHigherNumber = Math.max(absNumber1, absNumber2);
    double absLowerNumber = Math.min(absNumber1, absNumber2);
    double lcm = absHigherNumber;
    while (lcm % absLowerNumber != 0)
      lcm += absHigherNumber;
    if (verbose)
      println("[+] lcm of " + d + " and " + e + " is " + lcm);
    return lcm;
  }

  /**
   * Returns the least common multiple of the two integers passed.
   * 
   * @param num1 the first number
   * @param num2 the second number
   * @return the lcm
   * @since 0.1.0.1.3
   */
  public static long lcm(long num1, long num2) {
    if (num1 == 0 || num2 == 0)
      return 0;
    long absNumber1 = Math.abs(num1);
    long absNumber2 = Math.abs(num2);
    long absHigherNumber = Math.max(absNumber1, absNumber2);
    long absLowerNumber = Math.min(absNumber1, absNumber2);
    long lcm = absHigherNumber;
    while (lcm % absLowerNumber != 0)
      lcm += absHigherNumber;
    if (verbose)
      println("[+] lcm of " + num1 + " and " + num2 + " is " + lcm);
    return lcm;
  }

  /**
   * Finds the largest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the largest number in the array
   * @since 0.1.0.1.3
   */
  public static int max(int[] array) {
    int max = array[0];
    for (int i = 1; i < array.length; i++)
      if (array[i] > max)
        max = array[i];
    if (verbose)
      println("[+] max is " + max);
    return max;
  }

  /**
   * Finds the largest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the largest number in the array
   * @since 0.1.0.1.3
   */
  public static int max(int[][] array) {
    return max(to1DArray(array));
  }

  /**
   * Finds the largest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the largest number in the array
   */
  public static double max(double[] array) {
    double max = array[0];
    for (int i = 1; i < array.length; i++)
      if (array[i] > max)
        max = array[i];
    if (verbose)
      println("[+] max is " + max);
    return max;
  }

  /**
   * Finds the largest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the largest number in the array
   */
  public static double max(double[][] array) {
    return max(to1DArray(array));
  }

  /**
   * Finds the smallest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the smallest number in the array
   */
  public static int min(int[] array) {
    int minValue = array[0];
    for (int i = 1; i < array.length; i++)
      if (array[i] < minValue)
        minValue = array[i];
    if (verbose)
      println("[+] min is " + minValue);
    return minValue;
  }

  /**
   * Finds the smallest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the smallest number in the array
   */
  public static int min(int[][] array) {
    int[] longarray = to1DArray(array);
    int minValue = longarray[0];
    for (int i = 1; i < longarray.length; i++)
      if (longarray[i] < minValue)
        minValue = longarray[i];
    if (verbose)
      println("[+] min is " + minValue);
    return minValue;
  }

  /**
   * Finds the smallest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the smallest number in the array
   */
  public static double min(double[] array) {
    double minValue = array[0];
    for (int i = 1; i < array.length; i++)
      if (array[i] < minValue)
        minValue = array[i];
    if (verbose)
      println("[+] min is " + minValue);
    return minValue;
  }

  /**
   * Finds the smallest number that exists in the passed array.
   * 
   * @param array the array to use
   * @return the smallest number in the array
   */
  public static double min(double[][] array) {
    double[] longarray = to1DArray(array);
    double minValue = longarray[0];
    for (int i = 1; i < longarray.length; i++)
      if (longarray[i] < minValue)
        minValue = longarray[i];
    if (verbose)
      println("[+] min is " + minValue);
    return minValue;
  }

  /**
   * Finds the median of the passed array.
   * 
   * @param array the array to use
   * @return the median
   */
  public static double median(int[] array) {
    if (array.length % 2 == 0) {
      int leftindex = (int) Math.floor(array.length * 1.0 / 2);
      int leftelement = array[leftindex];
      int rightelement = array[leftindex - 1];
      return (leftelement + rightelement) / 2.0;
    } else
      return array[array.length / 2];
  }

  /**
   * Returns log base of a number.
   * 
   * @param base the base
   * @param num the number
   * @return log based a number
   * @since 0.1.5.3.1
   */
  public static double log(int base, double num) {
    if (verbose)
      println("[*] performing log base function");
    return Math.log(num) / Math.log(base);
  }

  /**
   * Calculates the distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   */
  public static double distance(Object[][] array, int[] from, int[] to) {
    return pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   */
  public static double distance(int[][] array, int[] from, int[] to) {
    return pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   */
  public static double distance(double[][] array, int[] from, int[] to) {
    return pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   */
  public static double distance(boolean[][] array, int[] from, int[] to) {
    return pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   */
  public static double distance(char[][] array, int[] from, int[] to) {
    return pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the inverted distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   * @since 0.1.5.2.3
   */
  public static double distanceInv(Object[][] array, int[] from, int[] to) {
    return array.length - pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the inverted distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   * @since 0.1.5.2.3
   */
  public static double distanceInv(int[][] array, int[] from, int[] to) {
    return array.length - pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the inverted distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   * @since 0.1.5.2.3
   */
  public static double distanceInv(double[][] array, int[] from, int[] to) {
    return array.length - pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the inverted distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   * @since 0.1.5.2.3
   */
  public static double distanceInv(boolean[][] array, int[] from, int[] to) {
    return array.length - pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Calculates the inverted distance between two coordinates in a 2D array.
   * 
   * @param array the array to use
   * @param from the first set of coordinates
   * @param to the second set of coordinates
   * @return the distance between the two
   * @since 0.1.5.2.3
   */
  public static double distanceInv(char[][] array, int[] from, int[] to) {
    return array.length - pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  /**
   * Finds the number in the options array closest to i.
   * 
   * @param i the integer
   * @param options numbers to compare
   * @return the int that i is closest to
   * @since 0.1.5.2.2
   */
  public static int closestTo(int i, int[] options) {
    int closest = options[0];
    for (int integer : options) {
      if (Math.abs(integer - i) < Math.abs(closest - i))
        closest = integer;
    }
    if (verbose)
      println("[+] closest number found: " + closest);
    return closest;
  }

  /**
   * Finds the number in the options array closest to i.
   * 
   * @param i the integer
   * @param options numbers to compare
   * @return the int that i is closest to
   * @since 0.1.5.2.2
   */
  public static double closestTo(double i, double[] options) {
    double closest = options[0];
    for (double integer : options)
      if (Math.abs(integer - i) < Math.abs(closest - i))
        closest = integer;
    if (verbose)
      println("[+] closest number found: " + closest);
    return closest;
  }

  /**
   * Finds the longer side of a right triangle.
   * 
   * @param a a
   * @param b b
   * @return c c
   * @since 0.1.1.2.4
   */
  public static double pyth(double a, double b) {
    if (verbose)
      println("[*] calculating pyth of " + a + " and " + b);
    return Math.sqrt(a * a + b * b);
  }

  /**
   * Adds two matrices.
   * 
   * @param matrix1 the first matrix
   * @param matrix2 the second matrix
   * @return the combined matrix
   * @since 0.1.5.2.7
   */
  public static int[][] add(int[][] matrix1, int[][] matrix2) {
    if (!Arrays.equals(getDims(matrix1), getDims(matrix2)))
      throw new IllegalArgumentException("[-] array dimensions must be equal");
    int[][] added = new int[matrix1.length][matrix1[0].length];
    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        added[i][j] = matrix1[i][j] + matrix2[i][j];
      }
    }
    return added;
  }

  /**
   * Adds two matrices.
   * 
   * @param matrix1 the first matrix
   * @param matrix2 the second matrix
   * @return the combined matrix
   * @since 0.1.5.2.7
   */
  public static double[][] add(double[][] matrix1, double[][] matrix2) {
    if (!Arrays.equals(getDims(matrix1), getDims(matrix2)))
      throw new IllegalArgumentException("[-] array dimensions must be equal");
    double[][] added = new double[matrix1.length][matrix1[0].length];
    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        added[i][j] = matrix1[i][j] + matrix2[i][j];
      }
    }
    return added;
  }

  /**
   * Subtracts two matrices.
   * 
   * @param matrix1 the first matrix
   * @param matrix2 the second matrix
   * @return the combined matrix
   * @since 0.1.5.2.7
   */
  public static int[][] subtract(int[][] matrix1, int[][] matrix2) {
    if (!Arrays.equals(getDims(matrix1), getDims(matrix2)))
      throw new IllegalArgumentException("[-] array dimensions must be equal");
    int[][] added = new int[matrix1.length][matrix1[0].length];
    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        added[i][j] = matrix1[i][j] - matrix2[i][j];
      }
    }
    return added;
  }

  /**
   * Subtracts two matrices.
   * 
   * @param matrix1 the first matrix
   * @param matrix2 the second matrix
   * @return the combined matrix
   * @since 0.1.5.2.7
   */
  public static double[][] subtract(double[][] matrix1, double[][] matrix2) {
    if (!Arrays.equals(getDims(matrix1), getDims(matrix2)))
      throw new IllegalArgumentException("[-] array dimensions must be equal");
    double[][] added = new double[matrix1.length][matrix1[0].length];
    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        added[i][j] = matrix1[i][j] - matrix2[i][j];
      }
    }
    return added;
  }

  // ------------------------------RANDOM-------------------------------------

  /**
   * Randomly returns either true or false.
   * 
   * @return true or false
   * @since 0.1.5.1.1
   */
  public static boolean coinflip() {
    return new Random().nextBoolean();
  }

  /**
   * Rolls a dice, giving a 1/sides chance of returning true.
   *
   * For example, a 1/6 chance would return true once every six method executions.
   * 
   * @param sides the amount of sides on the dice
   * @return true if the chance is met, false otherwise
   * @since 0.1.4.1.1
   */
  public static boolean diceroll(int sides) {
    return randomInt(0, sides) == 0;
  }

  /**
   * Generates a random number within the range specified.
   * 
   * @param min the minimum
   * @param max the maximum
   * @return a random number between min and max
   * @since 0.1.4.1.1
   */
  public static double random(int min, int max) {
    return (new Random().nextDouble() * (max - min)) + min;
  }

  /**
   * Generates a random number within the range specified.
   * 
   * Min is inclusive, while max is not
   * 
   * @param min the minimum
   * @param max the maximum
   * @return a random number between min and max
   * @since 0.1.4.1.1
   */
  public static int randomInt(int min, int max) {
    if (min < max)
      return min + new Random().nextInt(Math.abs(max - min));
    return min - new Random().nextInt(Math.abs(max - min));
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.4.1.1
   */
  public static int randomIndex(Object[] array) {
    return new Random().nextInt(array.length);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.4.1.1
   */
  public static int randomIndex(int[] array) {
    return new Random().nextInt(array.length);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.4.1.1
   */
  public static int randomIndex(double[] array) {
    return new Random().nextInt(array.length);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.4.1.1
   */
  public static int randomIndex(char[] array) {
    return new Random().nextInt(array.length);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.5.3.4
   */
  public static Point randomIndex(Object[][] array) {
    int randomI = Msn.randomInt(0, array.length);
    int randomJ = Msn.randomInt(0, array[randomI].length);
    return new Point(randomI, randomJ);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.5.3.4
   */
  public static Point randomIndex(int[][] array) {
    int randomI = Msn.randomInt(0, array.length);
    int randomJ = Msn.randomInt(0, array[randomI].length);
    return new Point(randomI, randomJ);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.5.3.4
   */
  public static Point randomIndex(double[][] array) {
    int randomI = Msn.randomInt(0, array.length);
    int randomJ = Msn.randomInt(0, array[randomI].length);
    return new Point(randomI, randomJ);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.5.3.4
   */
  public static Point randomIndex(boolean[][] array) {
    int randomI = Msn.randomInt(0, array.length);
    int randomJ = Msn.randomInt(0, array[randomI].length);
    return new Point(randomI, randomJ);
  }

  /**
   * Gets a random index of the array specified.
   * 
   * @param array the array
   * @return a random index
   * @since 0.1.5.3.4
   */
  public static Point randomIndex(char[][] array) {
    int randomI = Msn.randomInt(0, array.length);
    int randomJ = Msn.randomInt(0, array[randomI].length);
    return new Point(randomI, randomJ);
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param <T> Generic
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static <T> T randomElement(T[] array) {
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static int randomElement(int[] array) {
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static double randomElement(double[] array) {
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static char randomElement(char[] array) {
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param <T> Generic
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static <T> T randomElement(T[][] array) {
    ArrayList<T> list = new ArrayList<>();
    importAll(array, list);
    Collections.shuffle(list);
    return list.get(0);
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static int randomElement(int[][] array) {
    ArrayList<Integer> list = new ArrayList<>();
    importAll(array, list);
    Collections.shuffle(list);
    return list.get(0);
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static double randomElement(double[][] array) {
    ArrayList<Double> list = new ArrayList<>();
    importAll(array, list);
    Collections.shuffle(list);
    return list.get(0);
  }

  /**
   * Gets a random element in the array passed.
   * 
   * @param array the array to search
   * @return a random element
   * @since 0.1.4.1.1
   */
  public static char randomElement(char[][] array) {
    ArrayList<Character> list = new ArrayList<>();
    importAll(array, list);
    Collections.shuffle(list);
    return list.get(0);
  }

  /**
   * Gets a random letter from the English alphabet.
   * 
   * @return a random letter
   * @since 0.1.4.1.1
   */
  public static char randomLetter() {
    return randomElement(alphabetArray());
  }

  /**
   * Generates a random String using the letters from the English alphabet.
   * 
   * @param length the preferred length of the String
   * @return the randomized String
   * @since 0.1.4.1.1
   */
  public static String randomString(int length) {
    String s = "";
    for (int i = 0; i < length; i++)
      s += Msn.randomLetter();
    return s;
  }

  /**
   * Generates a word with length 5 that can be pronounced.
   * 
   * @return a String
   * @since 0.1.5.2.8
   */
  public static String randomWord() {
    return String.valueOf(randomElement(consonants())) + String.valueOf(randomElement(vowels()))
        + String.valueOf(randomElement(consonants())) + String.valueOf(randomElement(vowels()))
        + String.valueOf(randomElement(consonants()));
  }

  /**
   * Generates a random Color with random red, green and blue values.
   * 
   * @return a new Color
   * @since 0.1.5.3.1
   */
  public static Color randomColor() {
    return new Color(randomInt(0, 256), randomInt(0, 256), randomInt(0, 256));
  }

  /**
   * Gets a random Color from the AWT Color collection.
   * 
   * @return a random Color
   * @since 0.1.5.3.1
   */
  public static Color randomAwtColor() {
    return (Color) randomElement(awtColorArray());
  }

  /**
   * Shuffles the given array.
   * 
   * @param array the array to shuffle
   * @since 0.1.5.2.6
   */
  public static void shuffle(Object[] array) {
    ArrayList<Object> list = new ArrayList<>(List.of(array));
    Collections.shuffle(list);
    for (int i = 0; i < array.length; i++)
      array[i] = list.get(i);
  }

  /**
   * Shuffles the given array.
   * 
   * @param array the array to shuffle
   * @since 0.1.5.2.6
   */
  public static void shuffle(int[] array) {
    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(box(array)));
    Collections.shuffle(list);
    for (int i = 0; i < array.length; i++)
      array[i] = list.get(i);
  }

  /**
   * Shuffles the given array.
   * 
   * @param array the array to shuffle
   * @since 0.1.5.2.6
   */
  public static void shuffle(double[] array) {
    ArrayList<Double> list = new ArrayList<>(Arrays.asList(box(array)));
    Collections.shuffle(list);
    for (int i = 0; i < array.length; i++)
      array[i] = list.get(i);
  }

  /**
   * Shuffles the given array.
   * 
   * @param array the array to shuffle
   * @since 0.1.5.2.6
   */
  public static void shuffle(boolean[] array) {
    ArrayList<Boolean> list = new ArrayList<>(Arrays.asList(box(array)));
    Collections.shuffle(list);
    for (int i = 0; i < array.length; i++)
      array[i] = list.get(i);
  }

  /**
   * Shuffles the given array.
   * 
   * @param array the array to shuffle
   * @since 0.1.5.2.6
   */
  public static void shuffle(char[] array) {
    ArrayList<Character> list = new ArrayList<>(Arrays.asList(box(array)));
    Collections.shuffle(list);
    for (int i = 0; i < array.length; i++)
      array[i] = list.get(i);
  }

  // ----------------------------COMBO/PERMU-----------------------------------

  /**
   * Calculates all permutations of the array passed.
   * 
   * @param arr the array
   * @return all permutations of the array passed
   */
  public static List<List<Integer>> permute(int[] arr) {
    List<List<Integer>> list = new ArrayList<>();
    ph(list, new ArrayList<>(), arr);
    return list;
  }

  /**
   * Recursive permutation assistance.
   * 
   * @param list the current list of permutations.
   * @param result current permutation
   * @param a the array
   */
  private static void ph(List<List<Integer>> list, List<Integer> result, int[] a) {
    if (result.size() == a.length)
      list.add(new ArrayList<>(result));
    else {
      for (int i = 0; i < a.length; i++) {
        if (result.contains(a[i]))
          continue;
        result.add(a[i]);
        ph(list, result, a);
        result.remove(result.size() - 1);
      }
    }
  }

  // --------------------------------SOUND-------------------------------------

  /**
   * Plays a beep sound
   * 
   * @since 0.1.5.1.3
   */
  public static void beep() {
    Toolkit.getDefaultToolkit().beep();
    if (verbose)
      println("[+] beep sound played");
  }

  /**
   * Plays an audio file.
   * 
   * @param path
   * @since 0.1.5.3.4
   */
  public static void playWav(String path, double gain) {
    File f = new File("./" + path);
    AudioInputStream audioIn = null;
    try {
      audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
    } catch (UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
    }
    Clip clip = null;
    try {
      clip = AudioSystem.getClip();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
    try {
      clip.open(audioIn);
    } catch (LineUnavailableException | IOException e) {
      e.printStackTrace();
    }
    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue((float) gain);
    clip.start();
  }

  // ---------------------------SUB CLASSES-------------------------------------

  /**
   * Class for Multimap capabilities. This is a full duplication of the MsnMultimap. Duplication was
   * made to avoid having to move two classes to allow Msn to compile.
   * 
   * @author Mason Marker
   * @version 1.0 - 05/03/2021
   * @since 0.1.5.2.5
   */
  public static class embMsnMultimap<K, V> implements Iterable<Map.Entry<K, V>> {

    private HashMap<K, ArrayList<V>> map;

    /**
     * Multimap constructor.
     */
    public embMsnMultimap() {
      map = new HashMap<>();
    }

    /**
     * Method for distributing elements.
     * 
     * @param k k
     * @param v v
     */
    public void put(K k, V v) {
      ArrayList<V> curr = map.get(k);
      if (curr == null) {
        ArrayList<V> list = new ArrayList<>();
        list.add(v);
        map.put(k, list);
      } else {
        curr.add(v);
      }
    }

    /**
     * Gets the set of values for the specified key.
     * 
     * @param key the key
     * @return the list of values that correspond to the key
     */
    public ArrayList<V> get(K key) {
      return map.get(key);
    }

    /**
     * Checks if this map contains the key specified.
     * 
     * @param key the key to search for
     * @return whether the key was found or not
     */
    public boolean containsKey(K key) {
      return map.containsKey(key);
    }

    /**
     * Checks if this map contains the value specified.
     * 
     * @param value the value to search for
     * @return whether the value was found or not
     */
    public boolean containsValue(V value) {
      Iterator<Entry<K, V>> i = iterator();
      while (i.hasNext()) {
        if (i.next().getValue().equals(value))
          return true;
      }
      return false;
    }

    public boolean containsEntry(K key, V value) {
      for (Map.Entry<K, V> entry : entryList()) {
        if (entry.getKey().equals(key) && entry.getValue().equals(value))
          return true;
      }
      return false;
    }

    public List<Entry<K, V>> entryList() {
      ArrayList<Entry<K, V>> list = new ArrayList<>();
      Iterator<Entry<K, V>> i = iterator();
      while (i.hasNext())
        list.add(i.next());
      return list;
    }

    public String toString() {
      return map.toString();
    }

    /**
     * Iterates through entries.
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
      return new Iterator<Map.Entry<K, V>>() {

        private Iterator<Map.Entry<K, ArrayList<V>>> iterator = map.entrySet().iterator();
        private Map.Entry<K, ArrayList<V>> currententry = iterator.next();
        private Iterator<V> narrowit = currententry.getValue().iterator();

        @Override
        public boolean hasNext() {
          return narrowit.hasNext() || iterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
          if (narrowit.hasNext())
            return Map.entry(currententry.getKey(), narrowit.next());
          currententry = iterator.next();
          narrowit = currententry.getValue().iterator();
          return Map.entry(currententry.getKey(), narrowit.next());
        }
      };
    }

  }

}
