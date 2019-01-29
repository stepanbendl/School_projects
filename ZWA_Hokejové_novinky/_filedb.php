<?php
    // přístup do databáze uživatelů

    $fileDBName = "db.json";

    $data = file_get_contents($fileDBName);

    $data = json_decode($data, JSON_OBJECT_AS_ARRAY);
    
    $users = $data["users"];
	

?>