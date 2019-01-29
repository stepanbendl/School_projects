<?php
$style1 = '<link rel="stylesheet" href="main.css" media="all">';
$style2 = '<link rel="stylesheet" href="main2.css" media="all">';

$articleStyle1 = '<link rel="stylesheet" href="article.css" media="all">';
$articleStyle2 = '<link rel="stylesheet" href="article2.css" media="all">';

$style = isset($_COOKIE['style']) ? $_COOKIE['style'] : 1;

if (isset($_POST['submit'])){
    $style=$_POST['skin'];
    setcookie('style', $style, time()+3600*24*365, "/", ".toad.cz");
}

$styles = $style == 1 ? $style1 : $style2;
$articleStyles = $style == 1 ? $articleStyle1 : $articleStyle2;