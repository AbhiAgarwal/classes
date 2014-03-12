close all
clear all
dt = 0.01; t1=1000;
time = 0:dt:t1;

%average life time in years
delta = 1/80;
beta = 3 * delta;

%average life time of infected person in years
delta_prime = 1/30;
beta_prime = 0.5*delta_prime;

trial = 1;
a_range = 0:0.01:0.3;

for a = a_range
    
    NStep = length(time);
    S = zeros(1, NStep);
    I = zeros(1, NStep);
    R = zeros(1, NStep);
    N = zeros(1, NStep);
    
    %growth rate
    lambda = zeros(1, NStep - 1);
    
    k = 1;
    S(1) = 950;
    I(1) = 50;
    N(1) = S(1) + I(1);

    for t = dt:dt:t1
        S(k + 1) = S(k) + dt * (-delta * S(k) + beta * S(k) + beta_prime*I(k) - a*(I(k)/N(k))*S(k));
        I(k + 1) = I(k) + dt * (-delta_prime *I(k) + a * (I(k)/N(k)) * S(k));
        N(k + 1) = S(k + 1) + I(k + 1);
        lambda(k) = (N(k+1) - N(k))/N(k)*dt;
        k = k + 1;
    end
    
    s_st(trial) = S(NStep)/N(NStep);
    i_st(trial) = I(NStep)/N(NStep);
    lambda_st(trial) = lambda(NStep - 1);
    
    figure(1);
    subplot(2, 1, 1); 
    hold on;
    plot(time, [S./N;I./N]);
    subplot(2, 1, 2);
    hold on;
    %Plot lambda with time, axes with same length
    plot(time(1:NStep - 1), lambda, 'r');

    trial = trial + 1;
end

figure;
subplot(2, 1, 1);
plot(a_range, [s_st;i_st]);
subplot(2, 1, 2);
plot(a_range, lambda_st, 'r');
hold on;
plot(xlim, [0 0], 'k--');