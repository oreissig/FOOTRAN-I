       REWIND 3
       DO 3 I = 1,N
       DO 3 J = 1,N
       IF(A(I,J)-A(J-I)) 3,20,3
    3  CONTINUE
       END FILE 3
C      MORE PROGRAM
       
   20  IF(I-J) 21,3,21
   21  WRITE TAPE 3,I,J, A(I,J)
       GO TO 3
