<?php
include("skins.php");
//Zjištění, zda je uživatel přihlášený, pokud ano, rovnou přesměrovat na index.php
session_start();

$logged = isset($_SESSION['luserId']);
if(!$logged){
    session_unset();
    session_destroy();
} else {
    header("Location: index.php");
}

//přidání databáze uživatelů a jejich identifikace
include("_filedb.php");
/**
 * @param $users
 * @param $username
 * @return bool
 */
function getUserByName($users, $username){
	foreach($users as $user){
		if($user['name'] == $username){
			return $user;
			}

		}
	return false;
	}

/**
 * @param $users
 * @param $uid
 * @return bool
 */
function getUserById($users, $uid) {
	foreach($users as $user){
		if($user['id'] == $uid){
			return $user;
		}
	}
	return false;
}
// pokud byl formulář odeslán

if (isset($_POST['login'])) {
    //vyplněny oba údaje
    $username = isset($_POST['jmeno']) ? $_POST['jmeno'] : false;
    $password = isset($_POST['heslo']) ? $_POST['heslo'] : false;

    if ($username && $password){
        //získání uživatele z DB
        $user = getUserByName($users, $username);
        
    // validace jména a hesla, spuštění session
        if ($user) {
            if(password_verify($password, $user['heslo'])) {
                session_start();
                $_SESSION['luserId'] = $user['id'];
                header("Location: index.php");

            } else {
                $error = "Přihlášení neproběhlo";
            }
        } else {
            $error = "Přihlášení neproběhlo";
        }
    } else {
        $error = "Prosím vyplňte všechny údaje";
    }
}

?>
<!DOCTYPE html>
<html>
<head>
	<title>Login │ Hokejové novinky</title>
	<meta charset="utf-8">
    <?php echo $styles; ?>
    <link rel="stylesheet" type="text/css" href="print.css" media="print">
    <script>
		function load(){
		    //uložení elementů do proměnných
			var form = document.getElementById('login');
			var jmeno = document.getElementById('jmeno');
			var heslo = document.getElementById('heslo');

            //přiřazení event listenerů
			form.addEventListener("submit", validace); // validace on submit
			jmeno.addEventListener('focus', odstranError);//pokud se do políčka klikne, odstraní se červený okraj
			heslo.addEventListener('focus', odstranError);
		}

		function validace(event){
            //uložení elementů do proměnných
			var jmeno = document.getElementById('jmeno');
			var heslo = document.getElementById('heslo');

			//pokud mají elementy classu error, odstranit jí
			jmeno.classList.remove("error");
			heslo.classList.remove("error");

            //ve jméně musí být text, pokud ne, neodesílat, přidat classu error a vyhodit upozornění
			if(jmeno.value.length < 1){
				event.preventDefault();
				jmeno.classList.add("error");
				alert("Jméno musí obsahovat alespoň jeden znak");
			}

            //heslo musí výt alespoň 7 znaků dlouhé a obsahovat malé písmeno, velké písmeno a číslici, pokude ne, neodesílat, přidat classu error a vyhodit upozornění
			if(heslo.value.length < 7 || !/\d/.test(heslo.value) || !/[a-z]/.test(heslo.value) || !/[A-Z]/.test(heslo.value)){
				event.preventDefault();
				heslo.classList.add("error");
				alert("Heslo musí mít alespoň 7 znaků a obsahovat[a-z], [A-Z], [0-9]");	
			}
		}

		//odstranit classu error pokud se klikne do políčka
		function odstranError(event) {
			event.target.classList.remove('error');
		}
	</script>
	<style>
		footer {position:fixed;}
	</style>
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
			<form method="post" id="login">
					<div class="row">
						<label>
							<input required id="jmeno" type="text" name="jmeno" placeholder="Username*" value="<?php if(isset($_POST['jmeno'])){echo htmlspecialchars($_POST['jmeno']);}else{echo "";}?>">
						</label>
					</div>
					<div class="row">
						<label>
							<input required type="Password" name="heslo"
							id="heslo" placeholder="Password*" autocomplete="off" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{7,}" title="Heslo musí mít alespoň 7 znaků a obsahovat[a-z], [A-Z], [0-9]">
						</label>
					</div>
					<div class="row">
						<label>
							<input type="submit" name="login" value="Login">
						</label>
					</div>
			</form>
            <div class="error-text">
            <?php
            //pokud se něco pokazilo, vyechovat co
                if (isset($error)) {
                    echo $error;
                }
            ?>
            </div>
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