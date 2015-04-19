.text
main:
addi $sp, $sp,-104
sw $t0, 100($sp)
sw $t1, 96($sp)
sw $t2, 92($sp)
sw $t3, 88($sp)
sw $t4, 84($sp)
sw $t5, 80($sp)
sw $t6, 76($sp)
sw $t7, 72($sp)
sw $t8, 68($sp)
sw $t9, 64($sp)
sw $s0, 60($sp)
sw $s1, 56($sp)
sw $s2, 52($sp)
sw $s3, 48($sp)
sw $s4, 44($sp)
sw $s5, 40($sp)
sw $s6, 36($sp)
sw $s7, 32($sp)
sw $a0, 28($sp)
sw $a1, 24($sp)
sw $a2, 20($sp)
sw $a3, 16($sp)
sw $at, 12($sp)
sw $k0, 8($sp)
sw $k1, 4($sp)
sw $ra, 0($sp)
li $a0,64
jal _new_object
lw $ra, 0($sp)
lw $k1, 4($sp)
lw $k0, 8($sp)
lw $at, 12($sp)
lw $a3, 16($sp)
lw $a2, 20($sp)
lw $a1, 24($sp)
lw $a0, 28($sp)
lw $s7, 32($sp)
lw $s6, 36($sp)
lw $s5, 40($sp)
lw $s4, 44($sp)
lw $s3, 48($sp)
lw $s2, 52($sp)
lw $s1, 56($sp)
lw $s0, 60($sp)
lw $t9, 64($sp)
lw $t8, 68($sp)
lw $t7, 72($sp)
lw $t6, 76($sp)
lw $t5, 80($sp)
lw $t4, 84($sp)
lw $t3, 88($sp)
lw $t2, 92($sp)
lw $t1, 96($sp)
lw $t0, 100($sp)
addi $sp, $sp, 104
move $1, $v0
addi $sp, $sp,-104
sw $t0, 100($sp)
sw $t1, 96($sp)
sw $t2, 92($sp)
sw $t3, 88($sp)
sw $t4, 84($sp)
sw $t5, 80($sp)
sw $t6, 76($sp)
sw $t7, 72($sp)
sw $t8, 68($sp)
sw $t9, 64($sp)
sw $s0, 60($sp)
sw $s1, 56($sp)
sw $s2, 52($sp)
sw $s3, 48($sp)
sw $s4, 44($sp)
sw $s5, 40($sp)
sw $s6, 36($sp)
sw $s7, 32($sp)
sw $a0, 28($sp)
sw $a1, 24($sp)
sw $a2, 20($sp)
sw $a3, 16($sp)
sw $at, 12($sp)
sw $k0, 8($sp)
sw $k1, 4($sp)
sw $ra, 0($sp)
move $a0, $1
li $a1, 10
jal Fac_ComputeFac
lw $ra, 0($sp)
lw $k1, 4($sp)
lw $k0, 8($sp)
lw $at, 12($sp)
lw $a3, 16($sp)
lw $a2, 20($sp)
lw $a1, 24($sp)
lw $a0, 28($sp)
lw $s7, 32($sp)
lw $s6, 36($sp)
lw $s5, 40($sp)
lw $s4, 44($sp)
lw $s3, 48($sp)
lw $s2, 52($sp)
lw $s1, 56($sp)
lw $s0, 60($sp)
lw $t9, 64($sp)
lw $t8, 68($sp)
lw $t7, 72($sp)
lw $t6, 76($sp)
lw $t5, 80($sp)
lw $t4, 84($sp)
lw $t3, 88($sp)
lw $t2, 92($sp)
lw $t1, 96($sp)
lw $t0, 100($sp)
addi $sp, $sp, 104
move $1, $v0
addi $sp, $sp,-104
sw $t0, 100($sp)
sw $t1, 96($sp)
sw $t2, 92($sp)
sw $t3, 88($sp)
sw $t4, 84($sp)
sw $t5, 80($sp)
sw $t6, 76($sp)
sw $t7, 72($sp)
sw $t8, 68($sp)
sw $t9, 64($sp)
sw $s0, 60($sp)
sw $s1, 56($sp)
sw $s2, 52($sp)
sw $s3, 48($sp)
sw $s4, 44($sp)
sw $s5, 40($sp)
sw $s6, 36($sp)
sw $s7, 32($sp)
sw $a0, 28($sp)
sw $a1, 24($sp)
sw $a2, 20($sp)
sw $a3, 16($sp)
sw $at, 12($sp)
sw $k0, 8($sp)
sw $k1, 4($sp)
sw $ra, 0($sp)
move $a0, $1
jal _system_out_println
lw $ra, 0($sp)
lw $k1, 4($sp)
lw $k0, 8($sp)
lw $at, 12($sp)
lw $a3, 16($sp)
lw $a2, 20($sp)
lw $a1, 24($sp)
lw $a0, 28($sp)
lw $s7, 32($sp)
lw $s6, 36($sp)
lw $s5, 40($sp)
lw $s4, 44($sp)
lw $s3, 48($sp)
lw $s2, 52($sp)
lw $s1, 56($sp)
lw $s0, 60($sp)
lw $t9, 64($sp)
lw $t8, 68($sp)
lw $t7, 72($sp)
lw $t6, 76($sp)
lw $t5, 80($sp)
lw $t4, 84($sp)
lw $t3, 88($sp)
lw $t2, 92($sp)
lw $t1, 96($sp)
lw $t0, 100($sp)
addi $sp, $sp, 104

jal _system_exit
Fac_ComputeFac:
li $9, 1
li $1, 2
add $8, $9, $1
sw $8, 4($4)

slt $3, $9, $1
lw $26, 4($4)
slt $8, $9, $26
seq $3, $3, $8
slti $3, $3, 1
beq $3, $zero, _t3
li $1, 1
j _t4
_t3:
li $1, 0
_t4:
slt $1, $9, $1
beq $1, $zero, _t9
li $1, 4
j _t10
_t9:
li $1, 5
_t10:
move $v0, $1
jr $ra

jal _system_exit
# main is testing the functions I've provided. You will include this code at the end
# of your output file so that you may call these system services.

#main:
#	li $a0, 100
#	jal _new_array
#	move $s0, $v0
#	move $a0, $v0
#	jal _system_out_println
#	lw $a0, 0($s0)
#	jal _system_out_println
#	jal _system_exit

_system_exit:
	li $v0, 10 #exit
	syscall
	
# Integer to print is in $a0. 
# Kills $v0 and $a0
_system_out_println:
	# print integer
	li  $v0, 1 
	syscall
	# print a newline
	li $a0, 10
	li $v0, 11
	syscall
	jr $ra
	
# $a0 = number of bytes to allocate
# $v0 contains address of allocated memory
_new_object:
	# sbrk
	li $v0, 9 
	syscall
	
	#initialize with zeros
	move $t0, $a0
	move $t1, $v0
_new_object_loop:
	beq $t0, $zero, _new_object_exit
	sb $zero, 0($t1)
	addi $t1, $t1, 1
	addi $t0, $t0, -1
	j _new_object_loop
_new_object_exit:
	jr $ra
	
# $a0 = number of bytes to allocate 
# $v0 contains address of allocated memory (with offset 0 being the size)	
_new_array:
	# add space for the size (1 integer)
	addi $a0, $a0, 4
	# sbrk
	li $v0, 9
	syscall
#initialize to zeros
	move $t0, $a0
	move $t1, $v0
_new_array_loop:
	beq $t0, $zero, _new_array_exit
	sb $zero, 0($t1)
	addi $t1, $t1, 1
	addi $t0, $t0, -1
	j _new_array_loop
_new_array_exit:
	#store the size (number of ints) in offset 0
	addi $t0, $a0, -4
	sra $t0, $t0, 2
	sw $t0, 0($v0)
	jr $ra