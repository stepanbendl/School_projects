<?php
// logout a přesměrování na index.php
session_start();
session_unset();
session_destroy();
header("Location: index.php");
