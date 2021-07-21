# proyectoMutantes
Poryecto api rest spring boot

La API Rest expuesta cuenta con dos metodos.

/mutant

HTTP method: POST

request body:
formato json

{"dna":[]}

El campo dna es madatorio para consumir el servicio el mismo debera ser una lista de strings, 
todas del mismo largo y la cantidad de caracteres por string debera ser igual a la catindad de items en la lista.
Los componentes validos de las cadenas de caracteres son unicamente las letras A, C, T y G
Ejemplo: ["ATGCGA","CTGCGC","TTATGT","AGAAGG","CACCTA","TCACTG"] (6 elementos de 6 caracteres de largo)

El servicio respondera con un status 200 en caso de que el dna suministrado sea el de un mutante y un 
status 403 en los demas casos.


/stats

HTTP method: GET

request body: N/A

El servicio respondera con las estadisticas de la cantidad de datos validos analizados y guardados en la base de datos
Los campos que contiene la respuesta son
totalMuestas -> cantidad toal de muestras validas que han sido procesadas (sin repetidos)
muestrasMutantes -> cantidad de muestras que corresponden a adn mutantes
ratio -> muestrasMutantes/totalMuestras
Ejemplo:

{
    "totalMuestas": 2,
    "muestrasMutantes": 2,
    "ratio": 1.0
}
