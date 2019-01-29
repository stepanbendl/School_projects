<?php
include("skins.php");
//zjištění zda je uživatel přihlášení, pokud není admin, přesměrování na index.php
session_start();

$logged = isset($_SESSION['luserId']);
if(!$logged){
    session_unset();
    session_destroy();
    header("Location: login.php");
    if($_SESSION['luserId'] != 1){
        header("Location: login.php");
    }
}
if($_SESSION['luserId'] != 1){
    header("Location: login.php");
}

//validace nadpisu a obsahu
/**
 * @param $nadpis
 * @return bool
 */
function validujNadpis($nadpis) {
    return strlen($nadpis) > 0;
}

/**
 * @param $obsah
 * @return bool
 */
function validujObsah($obsah) {
    return strlen($obsah) > 0;
}

$_SESSION['token'] = md5(session_id().time());
//otevření souboru s články
$articlesFile = "articles.json";
$articles = file_get_contents($articlesFile);
$articles = json_decode($articles, JSON_OBJECT_AS_ARRAY);
$data = $articles;

//byl formulář odeslán?
$nadpis = isset($_POST['nadpis']) ? htmlspecialchars($_POST['nadpis']) : '';
$obsah = isset($_POST['obsah']) ? $_POST['obsah'] : ''; //nechávám bez xss ochrany, aby mohl admin dělat odstavce a přidávat obrázky

if (count($_POST) > 0) {
    //byl formulář vyplněn správně?
    $validniNadpis = validujNadpis($nadpis);
    $validniObsah = validujobsah($obsah);

    if ($validniNadpis && $validniObsah) {
            //zápis do souboru
            if($logged){
            $count = count($data["articles"]);
            $sent = true;
            $newArticle = array(
                "id" => $count,
                "nadpis" => $nadpis,
                "obsah" => $obsah,
                "comments" => array()
            );
            array_push($data["articles"], $newArticle);

            file_put_contents($articlesFile, json_encode($data, JSON_FORCE_OBJECT));
            header("Location: index.php");
            }
        }
    } else {
        if((strlen($nadpis) + strlen($obsah)) != 0){
        $error = "Prosím vyplňte obě pole";}
        $sent = false;
    }

?>
<!DOCTYPE html>
<html>
<head>
	<title>Přidat článek │ Hokejové novinky</title>
	<meta charset="utf-8">
    <?php echo $styles; ?>
    <link rel="stylesheet" type="text/css" href="print.css" media="print">
    <script>
        function load(){
            //uložení elementů do proměnných
            var form = document.getElementById('add');
            var nadpis = document.getElementById('nadpis');
            var obsah = document.getElementById('obsah');

            //přiřazení event listenerů
            form.addEventListener("submit", validace); // validace on submit
            nadpis.addEventListener('focus', odstranError);//pokud se do políčka klikne, odstraní se červený okraj
            obsah.addEventListener('focus', odstranError);
        }

        function validace(event){
            //uložení elementů do proměnných
            var nadpis = document.getElementById('nadpis');
            var obsah = document.getElementById('obsah');

            //pokud mají elementy classu error, odstranit jí
            nadpis.classList.remove("error");
            obsah.classList.remove("error");

            //ve nadpisu musí být text, pokud ne, neodesílat, přidat classu error a vyhodit upozornění
            if(nadpis.value.length < 1){
                event.preventDefault();
                nadpis.classList.add("error");
                alert("Prosím vyplňte pole nadpis");
            }

            //ve nadpisu musí být text, pokud ne, neodesílat, přidat classu error a vyhodit upozornění
            if(obsah.value.length < 1){
                event.preventDefault();
                obsah.classList.add("error");
                alert("Prosím vyplňte pole obsah");
            }
        }

        //odstranit classu error pokud se klikne do políčka
        function odstranError(event) {
            event.target.classList.remove('error');
        }
    </script>
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
                <?php if($logged){echo "<li><a href='logout.php' class='logout'>Logout</a></li>";}   ?>
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
            <div class="error-text">
                <?php if(isset($error)){echo $error;} //pokud bylo něco špatně, zobraz to?>
            </div>
			<form method="post" id="add">
                <input type="hidden" name="token" value="<?php echo $_SESSION['token']?>">
				<div>
					<label>
						Nadpis: *<br>
						<input  required placeholder="Zde napište název článku" id="nadpis" type="text" name="nadpis" value="<?php
                        //předvyplněná pole a ochrana proti dvojímu odeslání
                        if(isset($_COOKIE['formSubmitted'])){
                            echo "";
                        } elseif (strlen($nadpis)>0 && !$sent){
                            echo "$nadpis";
                        } ?>">
					</label>
				</div>
				<div>	
					<label> Obsah článku: *<br>
						<textarea id="obsah"  placeholder="Zde napište článek" name="obsah"rows="30" cols="30"><?php
                            if(isset($_COOKIE['formSubmitted'])){
                                echo "";
                            } elseif(strlen($obsah)>0 && !$sent){
						        $obsah = htmlspecialchars($obsah);
                                echo "$obsah";
                        } ?></textarea>
                    </label>
				</div>
				<div>
					<input type="submit" name="odeslat" value="Přidat příspěvek" id="submitarticle">
				</div>
			</form>
            <script>
                load();
            </script>
		</div>
		<footer>
			Created by Štěpán Bendl. FEL ČVUT 2018.
		</footer>
	</article>
</body>
</html>