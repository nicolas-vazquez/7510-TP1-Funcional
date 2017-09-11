(ns parser
  (:require [clojure.string :as str]))

(defn valid-fact?
  [fact]
  (not (= nil (re-matches #"^[a-z0-9]+\([a-z0-9, ]+\)$" fact))))

(defn valid-rule?
  [rule]
  (not (= nil (re-matches #"^[a-z]+\(([A-Z]+, )*[A-Z]+\) :- (([a-z]+\(([A-Z]+, )*[A-Z]+\)), )*([a-z]+\(([A-Z]+, )*[A-Z]+\))" rule))))


(defn all-valid-facts? [facts]
  (if (every? true? (map valid-fact? facts)) facts))

(defn all-valid-rules? [rules]
  (if (every? true? (map valid-rule? rules)) rules))

(defn make-rule [rule]
  [(get (str/split rule #":-") 0)
    (str/split (str/replace (get (str/split rule #":-") 1) "),"")!") #"!")])

(defn normalize [string]
  (str/replace string #"(\ |\n|\t)" ""))

(defn is-fact [facts fact]
  (= (some #(= % fact) facts) true))

(defn get-vars [fact]
  (subvec (str/split fact #"(\(|,|\))") 1))

(defn get-facts [data]
  (all-valid-facts? (map normalize (filter #(and (not (str/blank? %)) (not (.contains % ":-"))) (str/split data #"\.")))))

(defn get-rules [data]
  (map make-rule (map normalize (filter #(and (not (str/blank? %)) (.contains % ":-")) (str/split data #"\.")))))

(defn build-rule [rawParams actualParams actualFact]
  (map (fn [fact]
      (reduce (fn [p1 p2]
          (str/replace-first p1 p2 (nth actualParams (.indexOf rawParams p2)))) fact rawParams)) actualFact))

(defn search-rule [query rules]
  (first (filter #(.startsWith (nth % 0)
    (first (str/split query #"\("))) rules)))

(defn apply-rule? [rule facts query]
  (= (every? #(is-fact facts %) (build-rule (get-vars (nth rule 0)) (get-vars query) (nth rule 1))) true))
