clear all; 
clf;
close all;

numCities = 4;
time_simulated = 365 * 2; %number of days
clock_max = 365 * 8; %divide number of days into half-day intervals
dt = time_simulated / clock_max;

N_save = zeros(numCities, clock_max);
S_save = zeros(numCities, clock_max);
I_save = zeros(numCities, clock_max);
R_save = zeros(numCities, clock_max);

gsus = zeros(1, clock_max);
ginf = zeros(1, clock_max);
grec = zeros(1, clock_max);

I_peaks = zeros(1, clock_max);

N = [1000 500 400 1200];
S = [999 498 399 1199];
I = [1 2 1 1];
R = [0 0 0 0];

totalPopulation = sum(N);

%Testing thresholds
a = [0.05 0.02 0.04 0.28];
b = [0.07 0.03 0.06 0.04];

TravelSR = [0 0.013 0.027 0.03; 0.032 0 0.022 0.016; 0.015 0.04 0 0.05; 0.067 0.076 0.045 0]; 
TravelI = [0 0.012 0.018 0.019; 0.01 0 0.03 0.09; 0.08 0.05 0 0.011; 0.051 0.042 0.017 0];

k = 500;
for i = 1:numCities
    for j = 1:numCities
        if (i ~= j)
            TravelSR(i, j) = TravelSR(i, j) + k * 0.003;
            TravelI(i, j) = TravelI(i, j) + k * 0.003;
        end
    end
end



startedTravel = false;

figure;
set(gcf, 'double', 'on');
subplot(3, 3, 1);
pie_1 = pie([S(1)/totalPopulation I(1)/totalPopulation R(1)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
title(strcat('City 1, a = ', num2str(a(1)), ', b = ', num2str(b(1))));
subplot(3, 3, 3);
pie_2 = pie([S(2)/totalPopulation I(2)/totalPopulation R(2)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
title(strcat('City 2, a = ', num2str(a(2)), ', b = ', num2str(b(2))));
subplot(3, 3, 7);
pie_3 = pie([S(3)/totalPopulation I(3)/totalPopulation R(3)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
title(strcat('City 3, a = ', num2str(a(3)), ', b = ', num2str(b(3))));
subplot(3, 3, 9);
pie_4 = pie([S(4)/totalPopulation I(4)/totalPopulation R(4)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
title(strcat('City 4, a = ', num2str(a(4)), ', b = ', num2str(b(4))));
subplot(3, 3, 5);
pie_5 = pie([(S(4)/totalPopulation + S(3)/totalPopulation + S(2)/totalPopulation + S(1)/totalPopulation) (I(4)/totalPopulation + I(3)/totalPopulation + I(2)/totalPopulation + I(1)/totalPopulation) (R(4)/totalPopulation + R(3)/totalPopulation + R(2)/totalPopulation + R(1)/totalPopulation)], {'Susceptible', 'Infected', 'Recovered'});
title('Global');
drawnow;
hold on;

for clock = 1:clock_max
    t = clock * dt;
    if (clock >= (time_simulated / 8))
        startedTravel = true;
    end
        
    if startedTravel                                                                             
        for c = 1:numCities
            newlyInfected = 0;
            for s = 1:S(c)
                if (rand < (dt * a(c) * I(c) / N(c)))
                    newlyInfected = newlyInfected + 1;
                end
            end
            newlyRecovered = 0;
            for i = 1:I(c)
                if (rand < dt * b(c))
                    newlyRecovered = newlyRecovered + 1;
                end
            end

            S(c) = S(c) - newlyInfected;
            I(c) = I(c) + newlyInfected - newlyRecovered;
            R(c) = R(c) + newlyRecovered;
            
        end
    end
   
    
    for i = 1:numCities
            for j = i+1:numCities
                % Count traffic entering and leaving city i
                if(i ~= j)
                     % i -> j      
                     initS = S(i);
                     for s = 1:S(i)
                        if rand < (TravelSR(i, j) * dt) && (S(i) ~= 0 && (sum(N) >= R(j) + I(j) + S(j)))
                            S(i) = S(i) - 1;
                            S(j) = S(j) + 1;
                        end
                     end                     
                     
                     for inf = 1:I(i)
                        if rand < (TravelI(i, j) * dt) && (I(i) ~= 0 && (sum(N) >= R(j) + I(j) + S(j)))
                            I(i) = I(i) - 1;
                            I(j) = I(j) + 1;
                        end
                     end                   
                     
                     for r = 1:R(i)
                         if rand < (TravelSR(i, j) * dt) && (R(i) ~= 0 && (sum(N) >= R(j) + I(j) + S(j)))
                             R(i) = R(i) - 1;
                             R(j) = R(j) + 1;
                         end
                     end
                     
                     % j -> i
                     
                     for s = 1:S(j)
                        if rand < (TravelSR(j, i) * dt) && (S(j) ~= 0 && (sum(N) >= R(i) + I(i) + S(i)))
                            S(j) = S(j) - 1;
                            S(i) = S(i) + 1;
                        end
                     end                     
                     
                     newS = S(i);
                     for inf = 1:I(j)
                        if rand < (TravelI(j, i) * dt) && (I(j) ~= 0 && (sum(N) >= R(i) + I(i) + S(i)))
                            I(j) = I(j) - 1;
                            I(i) = I(i) + 1;
                        end
                     end
                     
                     for r = 1:R(j)
                         if rand < (TravelSR(j, i) * dt) && (R(j) ~= 0 && (sum(N) >= R(i) + I(i) + S(i)))
                            R(j) = R(j) - 1;
                            R(i) = R(i) + 1;
                         end
                     end            
                end
                
            end
        
%          N_save(i, clock) = S(i)+I(i)+R(i);
%          S_save(i, clock) = S(i);
%          I_save(i, clock) = I(i);
%          R_save(i, clock) = R(i);
%          I_peaks(1, clock) = I_save(i,  clock) + I_peaks(1, clock);
    end
    
    for i = 1:numCities
         N_save(i, clock) = S(i)+I(i)+R(i);
         S_save(i, clock) = S(i);
         I_save(i, clock) = I(i);
         R_save(i, clock) = R(i);
         I_peaks(1, clock) = I_save(i,  clock) + I_peaks(1, clock);
    end
    
    
    clf('reset')

    subplot(3, 3, 1);
    pie_1 = pie([S(1)/totalPopulation I(1)/totalPopulation R(1)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
    title(strcat('City 1, a = ', num2str(a(1)), ', b = ', num2str(b(1))));
    subplot(3, 3, 3);
    pie_2 = pie([S(2)/totalPopulation I(2)/totalPopulation R(2)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
    title(strcat('City 2, a = ', num2str(a(2)), ', b = ', num2str(b(2))));
    subplot(3, 3, 7);
    pie_3 = pie([S(3)/totalPopulation I(3)/totalPopulation R(3)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
    title(strcat('City 3, a = ', num2str(a(3)), ', b = ', num2str(b(3))));
    subplot(3, 3, 9);
    pie_4 = pie([S(4)/totalPopulation I(4)/totalPopulation R(4)/totalPopulation], {'Susceptible', 'Infected', 'Recovered'});
    title(strcat('City 4, a = ', num2str(a(4)), ', b = ', num2str(b(4))));
    subplot(3, 3, 5);
    pie_5 = pie([(S(4)/totalPopulation + S(3)/totalPopulation + S(2)/totalPopulation + S(1)/totalPopulation) (I(4)/totalPopulation + I(3)/totalPopulation + I(2)/totalPopulation + I(1)/totalPopulation) (R(4)/totalPopulation + R(3)/totalPopulation + R(2)/totalPopulation + R(1)/totalPopulation)], {'Susceptible', 'Infected', 'Recovered'});
    title('Global');
    drawnow;
    hold off;
     
end

%Output static data
figure;
title(strcat(strcat(strcat('dt = ', num2str(dt)),' k = '), num2str(k)));
set(gcf, 'double', 'on');
U = 1.2 * time_simulated;
x = 1:clock;
L = 1.1 * max(I_save(:), max(S_save(:), R_save(:)));

subplot(2, 4, 1);
plot(x, S_save(1, 1:clock), x, I_save(1, 1:clock), x, R_save(1, 1:clock));
title('City 1');
xlabel('Time');
ylabel('Num. of people');

subplot(2, 4, 2);
plot(x, S_save(2, 1:clock), x, I_save(2, 1:clock), x, R_save(2, 1:clock));
title('City 2');
xlabel('Time');
ylabel('Num. of people');
subplot(2, 4, 3);
plot(x, S_save(3, 1:clock), x, I_save(3, 1:clock), x, R_save(3, 1:clock));
title('City 3');
xlabel('Time');
ylabel('Num. of people');
subplot(2, 4, 4);
plot(x, S_save(4, 1:clock), x, I_save(4, 1:clock), x, R_save(4, 1:clock));
title('City 4');
xlabel('Time');
ylabel('Num. of people');


% --- General population --- %
L = 1.1 * max(N_save(:));
subplot(2, 4, 5);
plot(N_save(1,1:clock))
title('Total Population in City 1');
xlabel('Time');
ylabel('Num. of people');
axis([0 U 0 L]);
subplot(2, 4, 6);
plot(N_save(2,1:clock))
title('Total Population in City 2');
xlabel('Time');
ylabel('Num. of people');
axis([0 U 0 L]);
subplot(2, 4, 7);
plot(N_save(3,1:clock))
title('Total Population in City 3');
xlabel('Time');
ylabel('Num. of people');
axis([0 U 0 L]);
subplot(2, 4, 8);
plot(N_save(4,1:clock))
title('Total Population in City 4');
xlabel('Time');
ylabel('Num. of people');
axis([0 U 0 L]);