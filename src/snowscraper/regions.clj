(ns snowscraper.regions)

(def oceania
  ["Australia"
   "New Zealand"])

(def south-america
  ["Argentina"
   "Chile"])

(def europe
  ["Switzerland"
   "Finland"
   "Sweden"
   "Scotland"
   "Romania"
   "Norway"
   "Germany"
   "Liechtenstein"
   "Czech Rep."
   "Belgium"
   "Andorra"
   "Spain"
   "Italy"
   "Slovakia"
   "Bulgaria"
   "France"
   "Poland"
   "Austria"
   "Netherlands"])

(def canada
  ["Quebec"
   "Ontario"
   "Alberta"
   "British Columbia"])

(def usa
  ["North Carolina"
   "Maine"
   "Indiana"
   "Iowa"
   "Utah"
   "Michigan"
   "Vermont"
   "Wyoming"
   "Connecticut"
   "Montana"
   "Missouri"
   "New Mexico"
   "Washington"
   "Nevada"
   "South Dakota"
   "West Virginia"
   "Virginia"
   "Illinois"
   "Idaho"
   "Maryland"
   "Minnesota"
   "Wisconsin"
   "Tennessee"
   "Oregon"
   "Massachusetts"
   "Pennsylvania"
   "New Jersey"
   "Colorado"
   "New Hampshire"
   "Ohio"
   "Arizona"
   "California"])

(def all (flatten usa canada europe south-america oceania))
