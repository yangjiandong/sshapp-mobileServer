function initUiDcIncludes(m) {
  m.add("UI111", ["app/sys/rolelist.js"]);
  m.add("UI112", ["app/sys/userlist.js"]);
  m.add("UI113", ["app/sys/app_setup_list.js"]);
  m.add("UI114", ["app/sys/permissionslist.js"]);
  m.add("UI115", ["app/sys/moduledictlist.js"]);

  m.add("UI211", ["app/mob/hospitallist.js"]);
  m.add("UI212", ["app/mob/queryitemlist.js"]);
  m.add("UI213", ["app/mob/autorunsetup.class.js",
          "app/mob/autorunsetup_query_list.js"]);

  m.add("UI221", ["app/mob/timepointlist.js"]);
  m.add("UI222", ["app/mob/measuretypelist.js"]);
  m.add("UI223", ["app/mob/vitalsignitemlist.js"]);
  m.add("UI224", ["app/mob/skintestlist.js"]);

  m.add("UI99", ["app/mob/moblog_list.js"]);
}
