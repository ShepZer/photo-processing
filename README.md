# photo-processing

## Задание №3
Выделить содержимое компонент R, G, B и сохранить их  отдельных файлах в формате RGB24.

## Задание №4а
Проанализировать корреляционные свойства компонент R, G, B используя формулу оценки коэффициента корреляции.

![image](https://github.com/ShepZer/photo-processing/assets/82727746/0c6c4759-4937-467b-82b4-ba543d3c0461)

Вычислить оценки коэффицентов корреляции между каждой парой компонент изображения.

## Задание №5
Выполнить преобразование данных из формата RGB в YCbCr. Для компонент Y, Cb, Cr вычислить оценки коэффицентов корреляции между каждой парой компонент изображения.

## Задание №6
Сохранить содержиое каждой компоненты Y, Cb, Cr в отдельном файле в формате RGB24.

## Задание №7
Выполниить обратное преобразование данных из формата YCbCr в RGB. Для R, G, B вычислить PNSR по исходным и восстановленным данным.

## Задание №8   
Произвести децимацию компонент Cb и Cr в два раза по ширине и в два раза по высоте следующими пособами:

а) исключив строки и столбцы с четными номерами;

б) вычислив значения элементов прореженного изображениия как среднее арифметическое четырех смежных элементов из исходного изображения.

## Задание №9
Произвести восстановления исходного рамера для компонент Cb и Cr путем копироваания недостающих значений слева и/или сверху, после чего выполнить обратное преобразование из YCbCr в RGB, используя формулу 

![image](https://github.com/ShepZer/photo-processing/assets/82727746/0b1e1082-6af9-46af-b8fb-65ed28e0e297)

## Задание №10
Для компонент Cb, Cr, B, G и R вычислить значение PNSR по исходным и восстановленным данным.

## Задание №11 
Повторить задания 8-10, уменьшив размер компонент Cb и Cr в 4 раза по ширине и высоте.

## Задание №12
Построить гистограммы частот для компонент R, G, B, Y, Cb, Cr.

## Задание №13
Оценить число бит, затрачиваемых при поэлементном независимом сжатии компонент R, G, B, Y, Cb< Cr восспользовавшись формулой

![image](https://github.com/ShepZer/photo-processing/assets/82727746/7e0e4e41-d444-4bbd-a6b4-f8a82f5ea489)

## Задание №14
Требуется сформировать массивы размерности D_A^(r), где A указывает компоненту исходного изображения, для которой вычисляется разность, а r - номер правила вычисления размерности. Значения на позиции (i, j) следует вычислять по следующей формуле

![image](https://github.com/ShepZer/photo-processing/assets/82727746/fa219a50-72fb-4972-80a9-daff44de913e)

где a(i,j) - значение пикселя в компоненте A на позиции (i,j), а f^(r) (i,j) - правило с номером r, по которому вычисляется предсказаение пикселя на позиции (i,j). В исследовании следует использовать следующие правила предскащания:

  1 - сосед слева (i,j-1);
  
  2 - сосед сверху (i-1,j);
  
  3 - сосед сверзу слева (i-1,j-1);
  
  4 - среднее арифметическое трех соседей - сверзу, слева и сверху слева.
  
Пиксели, находящиеся в первой строке и в первом столбце из рассматрения исключить, так как они не имеют некоторых соседей для построения. Массивы следует построить для компонент R, G, B, Y, Cb, Cr.

## Задание №15
Для массива D_A^(r) сформированных в предыдущем задании, построить гистограммы частот и сравнить их с гистограммами значений соответствующих компонент.
