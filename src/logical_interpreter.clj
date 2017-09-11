(ns logical-interpreter
  (:require [parser :refer :all]))

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [database query]
  (def facts (get-facts database))
  (def rules (get-rules database))

  (let 
    [rule (search-rule (normalize query) rules)]
    (if (and (valid-fact? (normalize query)) (all-valid-facts? facts) (not (nil? rules)))
      (or (and (not (nil? rule)) (apply-rule? rule facts (normalize query))) (is-fact facts (normalize query))))))
