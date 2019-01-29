<?php
include("skins.php");
//Zjištění, zda je uživatel přihlášený
session_start();

$logged = isset($_SESSION['luserId']);
if(!$logged){
    session_unset();
    session_destroy();
}

// otevření souboru s články
$articlesFile = "articles.json";
$articles = file_get_contents($articlesFile);
$articles = json_decode($articles, JSON_OBJECT_AS_ARRAY);
$reversedArticles = array_reverse($articles['articles']); // články od nejnovějších

//stránkování
/**
 * @param $articles
 * @param $offset
 * @param $limit
 * @return array
 */
function getArticlesPaging($articles, $offset, $limit) {
    $articles = $articles;

    $last = min(($offset+$limit), count($articles));

    $a = [];
    for ($i = $offset; $i < $last; $i++) {
        array_push($a, $articles[$i]);
    }
    return $a;
}

$itemsPerPage = 12;
$totalItems = count($articles['articles']);



?>
<!DOCTYPE html>
<html>
<head>
    <title>Hokejové novinky</title>
    <meta charset="utf-8">
    <?php echo $styles; ?>
    <link rel="stylesheet" type="text/css" href="print.css" media="print">
</head>
<body>
<header>
    <a href="index.php"><img src="pictures/sticks.jpg" alt="logo" class="Image"></a>
    <nav>
        <ul>
            <li class="first-item"><a href="login.php">Login</a></li>
            <li><a href="index.php">Nejnovější články</a></li>
            <li><a class="<?php if($logged){
                    //Skrytí přidání článku v menu, pokud přihlášený není admin
                    if($_SESSION['luserId'] == 1){
                        echo "admin-here";
                    }else{
                        echo "admin-only";}
                }else{
                    echo "admin-only";
                } ?>" href="add.php">Přidat článek</a></li>
            <?php if($logged){echo "<li><a href='logout.php' class='logout'>Logout</a></li>";}?>
        </ul>
    </nav>
    <form method="post" class="skins">
        <label> Světlý
            <input type="radio" name="skin" value="1" <?php echo $style == 1 ? "checked" : "" ?>>
        </label>
        <label>Tmavý
            <input type="radio" name="skin" value="2" <?php echo $style == 2 ? "checked" : "" ?>>
        </label>
        <input type="submit" name="submit" value="Změnit styl">
    </form>
</header>
<article>

    <div class="content">
        <?php
        if($totalItems > 0) {
            $pages = ceil($totalItems / $itemsPerPage);
            $page = min($pages, filter_input(INPUT_GET, 'page', FILTER_VALIDATE_INT, array(
                'options' => array(
                    'default' => 1,
                    'min_range' => 1,
                ),
            )));
            $offset = ($page - 1) * $itemsPerPage;
            $headingsToDisplay = getArticlesPaging($reversedArticles, $offset, $itemsPerPage);

            //zobrazení nadpisů
            foreach($headingsToDisplay as $heading){
                $nadpis = $heading['nadpis'];
                //přidání id do odkazu
                $id = $heading['id'];
                echo "<a href='article.php?id=$id' class='article'>$nadpis</a>\n" ;
            }


           ?>
    </div>
    <?php
    if ($page > 1) {
        $previousLink = '<a href="?page=1">&laquo;</a> <a href="?page=' . ($page - 1) . '">&lsaquo;</a>';
    } else {
        $previousLink = "&laquo; &laquo;";
    }

    $links = "";

    for ($i = 0; $i < $pages; $i++) {
        if ($i == $page - 1) {
            $links .= " " . ($i + 1) . " ";
        } else {
            $links .= " <a href=\"?page=" . ($i + 1) . "\">" . ($i + 1) . "</a> ";
        }
    }

    if ($page < $pages) {
        $nextLink = '<a href="?page=' . ($page + 1) . '">&rsaquo;</a> <a href="?page=' . $pages . '">&raquo;</a>';
    } else {
        $nextLink = "&rsaquo; &raquo;";
    }
    }

    echo "<nav class='paging'> $previousLink $links $nextLink </nav>";
    ?>
    <footer>
        Created by Štěpán Bendl. FEL ČVUT 2018.
    </footer>
</article>
</body>
</html>