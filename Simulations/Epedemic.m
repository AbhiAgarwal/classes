close all
clear all

dt = 0.01; 
t1 = 50;
time = 0; 
dt = t1;
a = 1; 
b = 0.3; 
ratio = a/b;

program_range = 0.1:0.1:0.2;
trial = 1; 
Ntravel = length(program_range);

ss = zeros(Ntravel, 3); % steady states
i_max = zeros(Ntravel, 1);

Nstep = length(time);
s = zeros(1, Nstep);
i = zeros(1, Nstep);
r = zeros(1, Nstep);
k = 1;
s(1) = 0.9;
i(1) = 0.1;
r(1) = 0.0;

for b = program_range
   k = 1;
   for t = dt:dt:t1
       SI = dt * a * i(k) * s(k);
       IR = dt * b * i(k);
       s(k + 1) = s(k) - SI;
       i(k + 1) = i(k) + (SI - IR);
       r(k + 1) = r(k) + IR;
       k = k + 1;
   end
   ss(trial, :) = [s(end), i(end), r(end)];
   i_max(trial) = max(i);
   trial = trial + 1;
   hold on;
   drawnow;
end

figure;
plot(program_range, i_max);
figure;
plot(program_range, ss);