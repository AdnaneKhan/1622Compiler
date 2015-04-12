.text
main:
li $a0,4
jal _new_object
move $t0, $v0
move $a0, $t0
li $a1, 9
addi $sp, $sp,-88
sw $t0, 84($v0)
sw $t1, 80($v0)
sw $t2, 76($v0)
sw $t3, 72($v0)
sw $t4, 68($v0)
sw $t5, 64($v0)
sw $t6, 60($v0)
sw $t7, 56($v0)
sw $t8, 52($v0)
sw $t9, 48($v0)
sw $s0, 44($sp)
sw $s1, 40($sp)
sw $s2, 36($sp)
sw $s3, 32($sp)
sw $s4, 28($sp)
sw $s5, 24($sp)
sw $s6, 20($sp)
sw $s7, 16($sp)
sw $at, 12($sp)
sw $k0, 8($sp)
sw $k1, 4($sp)
sw $ra, 0($sp)
jal Test2_Start
lw $ra, 0($sp)
lw $k1, 4($sp)
lw $k0, 8($sp)
lw $at, 12($sp)
lw $s0, 16($sp)
lw $s1, 20($sp)
lw $s2, 24($sp)
lw $s3, 28($sp)
lw $s4, 32($sp)
lw $s5, 36($sp)
lw $s6, 40($sp)
lw $s7, 44($sp)
lw $t0, 48($sp)
lw $t1, 52($sp)
lw $t2, 56($sp)
lw $t3, 60($sp)
lw $t4, 64($sp)
lw $t5, 68($sp)
lw $t6, 72($sp)
lw $t7, 76($sp)
lw $t8, 80($sp)
lw $t9, 84($sp)
addi $sp, $sp, 88
move $t1, $v0
move $a0, $t1
jal _system_out_println
jal _system_exit
Test2_Start:
move $a0, $a0
move $a1, $a1
addi $sp, $sp,-88
sw $t0, 84($v0)
sw $t1, 80($v0)
sw $t2, 76($v0)
sw $t3, 72($v0)
sw $t4, 68($v0)
sw $t5, 64($v0)
sw $t6, 60($v0)
sw $t7, 56($v0)
sw $t8, 52($v0)
sw $t9, 48($v0)
sw $s0, 44($sp)
sw $s1, 40($sp)
sw $s2, 36($sp)
sw $s3, 32($sp)
sw $s4, 28($sp)
sw $s5, 24($sp)
sw $s6, 20($sp)
sw $s7, 16($sp)
sw $at, 12($sp)
sw $k0, 8($sp)
sw $k1, 4($sp)
sw $ra, 0($sp)
jal Test2_More
lw $ra, 0($sp)
lw $k1, 4($sp)
lw $k0, 8($sp)
lw $at, 12($sp)
lw $s0, 16($sp)
lw $s1, 20($sp)
lw $s2, 24($sp)
lw $s3, 28($sp)
lw $s4, 32($sp)
lw $s5, 36($sp)
lw $s6, 40($sp)
lw $s7, 44($sp)
lw $t0, 48($sp)
lw $t1, 52($sp)
lw $t2, 56($sp)
lw $t3, 60($sp)
lw $t4, 64($sp)
lw $t5, 68($sp)
lw $t6, 72($sp)
lw $t7, 76($sp)
lw $t8, 80($sp)
lw $t9, 84($sp)
addi $sp, $sp, 88
move $t2, $v0
move $v0, $t2
jr $ra

jal _system_exit
Test2_More:
li $t4, 5
addi $t4, $t4, 5
move $v0, $t4
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