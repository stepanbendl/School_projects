<?php
include("skins.php");
//Zjištění, zda je uživatel přihlášený
session_start();

$logged = isset($_SESSION['luserId']);
if(!$logged){
    session_unset();
    session_destroy();
}
//otevření souboru s články
$articlesFile = "articles.json";
$articles = file_get_contents($articlesFile);
$articles = json_decode($articles, JSON_OBJECT_AS_ARRAY);

$maxAId = count($articles['articles']) - 1;
$articleId = $_GET['id'];
//přístup ke správnému článku (id)
$found = false;
foreach ($articles['articles'] as $article){
    if($articleId == $article['id']){
        $articleToDisplay = $article['obsah'];
        $headingToDisplay = $article['nadpis'];
        $found = true;
        break;
    }
}

if($found == false){
    $articleId = $maxAId;
    foreach ($articles['articles'] as $article){
        if($articleId == $article['id']){
            $articleToDisplay = $article['obsah'];
            $headingToDisplay = htmlspecialchars($article['nadpis']);
            break;
        }
    }
}

// zápis komentáře
$commentName = isset($_POST['jmeno']) ? htmlspecialchars($_POST['jmeno']) : false;
$commentContent = isset($_POST['obsah']) ? htmlspecialchars($_POST['obsah']) : false;
$sent = isset($_POST['odeslat']) ? true : false;


if ($sent) {
        if ($logged) {
            if ($commentName && $commentContent) {
                $newComment = array(
                    "name" => $commentName,
                    "comment" => $commentContent
                );
                $_SESSION['formToken'] = md5(uniqid());

                array_push($articles['articles'][$articleId]['comments'], $newComment);
                file_put_contents($articlesFile, json_encode($articles, JSON_FORCE_OBJECT));
                header("Location: article.php?id=$articleId");
            } else {
                $error = "Prosím vyplňte obě pole";
            }
        } else {
            $error = "Pro přidávání komentářů se prosím přihlašte";
        }
    }

?>
<!DOCTYPE html>
<html>
<head>
	<title><?php echo $headingToDisplay; ?> | Hokejové novinky</title>
	<meta charset="utf-8">
    <?php echo $articleStyles; ?>
    <link rel="stylesheet" type="text/css" href="print.css" media="print">
    <script>
        function load(){
            //uložení elementů do proměnných
            var form = document.getElementById('commentForm');
            var jmeno = document.getElementById('jmeno');
            var comment = document.getElementById('comment');

            //přiřazení event listenerů
            form.addEventListener("submit", validace); // validace on submit
            jmeno.addEventListener('focus', odstranError);//pokud se do políčka klikne, odstraní se červený okraj
            comment.addEventListener('focus', odstranError);
        }

        function validace(event){
            //uložení elementů do proměnných
            var jmeno = document.getElementById('jmeno');
            var comment = document.getElementById('comment');

            //pokud mají elementy classu error, odstranit jí
            jmeno.classList.remove("error");
            comment.classList.remove("error");

            //ve nadpisu musí být text, pokud ne, neodesílat, přidat classu error a vyhodit upozornění
            if(jmeno.value.length < 1){
                event.preventDefault();
                jmeno.classList.add("error");
                alert("Prosím vyplňte pole jméno");
            }

            //ve nadpisu musí být text, pokud ne, neodesílat, přidat classu error a vyhodit upozornění
            if(comment.value.length < 1){
                event.preventDefault();
                comment.classList.add("error");
                alert("Prosím vyplňte pole komentář");
            }
        }

        //odstranit classu error pokud se klikne do políčka
        function odstranError(event) {
            event.target.classList.remove('error');
        }
    </script>
<body>
	<header>
		<a href="index.php"><img src="pictures/sticks.jpg" alt="logo" class="Image"></a>
		<nav>
			<ul>
					<li class="first-item"><a href="login.php">Login</a></li>
					<li><a href="index.php">Nejnovější články</a></li>
                    <li><a class="<?php
                        //Skrytí přidání článku v menu, pokud přihlášený není admin
                        if($logged){
                            if($_SESSION['luserId'] == 1){
                                echo "admin-here";
                            }else{
                                echo "admin-only";}
                        }else{
                            echo "admin-only";
                    } ?>" href="add.php">Přidat článek</a></li>
                <?php if($logged){echo "<li><a href='logout.php' class='logout'>Logout</a></li>";} //Pokud je uživatel přihlášený, zobrazí se logout možnost  ?>
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
            // Zobrazení článku
            
              echo "<h3>$headingToDisplay</h3>";
              echo "$articleToDisplay";


            ?>

            <form method="post" id="commentForm">
                <div>
                    <label>
                        Jméno: *<br>
                        <input required type="text" id="jmeno" name="jmeno" placeholder="Zadejte vaše jméno" value="<?php
                        //předvyplněná pole
                        if(isset($_POST['jmeno'])){
                            $jmeno = htmlspecialchars($_POST['jmeno']);
                            echo "$jmeno";
                        } ?>">
                    </label>
                </div>
                <div>
                    <span>Komentář:*</span><br>
                    <textarea id="comment" required placeholder="Zde napište komentář" name="obsah" rows="15" cols="8"><?php
                        //předvyplněná pole a ochrana proti dvojímu odeslání

                        if(isset($_POST['obsah'])){
                            $obsah = htmlspecialchars($_POST['obsah']);
                            echo "$obsah";
                        } ?></textarea>
                </div>
                <div>
                    <input type="submit" name="odeslat" id="submitarticle" value="Přidat komentář">
                </div>
            </form>
            <script>
                load();
            </script>

            <?php
                // zobrazení erroru
                if(isset($error)){
                    echo "<p class='error-text'>$error</p>";
                }
                //zobrazení komentářů
                echo "<p>Komentáře</p>";
                if(isset($article['comments'])){
                    foreach ($article['comments'] as $comment){
                        $nameToDisplay = $comment['name'];
                        $commentToDisplay = $comment['comment'];
                        echo "<h4>$nameToDisplay napsal / a:</h4>";
                        echo "<p>$commentToDisplay</p>";
                    }
                }
            ?>
		</div>
		<footer>
			Created by Štěpán Bendl. FEL ČVUT 2018.
		</footer>
	</article>
</body>
</html>