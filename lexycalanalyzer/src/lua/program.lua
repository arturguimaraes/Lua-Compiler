-- funcao que calcula a fatorial de um numero
-- recebe um numero "n"
function fat(n)
	if n == 0 then
		return 1
	else
		return n * fat(n - 1)
	end
end

-- imprime o fatorial
print("Fatorial: ", fat(5))