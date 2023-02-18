335CB Draganoiu Andreea

	Rezolvarea incepe in fisierul Tema2.java unde se primesc ca argumente in terminal
directorul fisierelor de intrare si numarul de tread-uri. Se pornesc P thread-uri
care citesc cate o linie din orders.txt, fiecare linie reprezentand o comanda.
	
	Order.java - se citeste o comanda cu un thread, iar pentru fiecare comanda
se pun in work pool toate produsele care trebuie cautate (numar maxim threaduri P).
Pentru a verifica daca o comanda este imcompleta(nu se gaseste numarul de produse)
e suficient sa verific daca nu exista ultimul produs. Folosesc 1 variabila booleana
pentru acest lucru: existLastProduct ramane adevarat cat timp exista sansa ca ultimul
produs sa existe (adica exista sansa ca toata comanda sa fie livrata) si devine fals
atunci cand de dovedeste comanda sa nu fie completa.
	
	Product.java - folosesc o variabila rank (trimisa ca parametru reprezentand al
catelea produs este) pentru ca thread-urile sa nu gaseasca mereu doar primul produs 
atunci cand se deschide fisierul. Folosesc a doua variabila booleana lastProduct care
e false la inceput pentru ca nu am ajuns la ultimul produs, si devine true atunci cand
se gaseste ultimul produs.
