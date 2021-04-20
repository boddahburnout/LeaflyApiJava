# LeaflyApiJava
A simple Api I made for easily accessing strain info directly from leafly

  This api takes a single string input and attempts to find a direct match strain and return all known strain data, if no direct match is found it query's a search,
and returns info on the first found search results. 


Example search and description lookup

LeaflySeach leaflyapi = new LeaflySearch();
JSONObject result = leaflyapi.fechLeafly("White Widow");
System.out.println(result.getDescriptionPlain(result));
