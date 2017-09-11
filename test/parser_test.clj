(ns parser-test
	(:require [clojure.test :refer :all]
		[logical-interpreter :refer :all]
		[parser :refer :all]))

(def database "
  rock(jagger).
  musician(jagger).
  musician(lennon).
  rockstar(X) :- rock(X), musician(X).")

(deftest valid-fact-test
	(testing "Passing a valid fact to valid-fact? should return true")
		(is (= (valid-fact? "male(manuel)") true))
		(is (= (valid-fact? "father(esteban, juan)") true)))

(deftest invalid-fact-test
	(testing "Passing an invalid fact to valid-fact? should return false")
		(is (= (valid-fact? "male(manuel") false))
		(is (= (valid-fact? "fatheresteban, juan)") false)))

(deftest valid-rule-test
	(testing "Passing a valid rule to valid-rule? should return true")
		(is (= (valid-rule? "hijo(X, Y) :- varon(X), padre(Y, X)") true))
		(is (= (valid-rule? "hija(X, Y) :- mujer(X), padre(Y, X)") true)))

(deftest invalid-rule-test
	(testing "Passing an invalid rule to valid-rule? should return false")
		(is (= (valid-rule? "hijoX, Y) :- varon(X), padre(Y, X)") false))
		(is (= (valid-rule? "hija(X, Y :- mujerX), padre(Y, X)") false)))

(deftest is-fact-test
	(testing "Passing a fact included in a set of facts should return true"
		(is (= (is-fact ["male(juan)" "female(juana)"] "female(juana)") true))))

(deftest get-vars-test
	(testing "Passing vars to get-vars should return parsed vars"
		(is (= (get-vars "fact(A,B)") ["A" "B"]))))

(deftest normalize-test
  (testing "Passing a dirty query should be returned normalized"
    (is (= (normalize "male (manuel) ") "male(manuel)"))))

(deftest apply-rule-test
	(let
		[
		facts (get-facts database)
		rules (get-rules database)
		]
		(testing "Passing a true rule to apply-rule? should return true"
      (is (= (apply-rule? (first rules) facts "rockstar(jagger)") true)))
		(testing "Passing a false rule to apply-rule? should return false"
      (is (= (apply-rule? (first rules) facts "rockstar(lennon)") false)))))