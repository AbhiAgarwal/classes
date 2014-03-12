%N-body problem

clear all
close all

G = 1;

%N = 3
%X = [0 -1 1]
%M = [100 0.1 0.1]
%V = [0 -sqrt(G * M(1) / 2) sqrt(G * M(1) / 2)]; %binary star

N = 5;
M = [100 0.1 0.1 0.1 0.1];
X = [0 1 2 3 4];
Y = zeros(1, N);
Z = zeros(1, N);
U = zeros(1, N);
V = [0 sqrt(G * M(1)./X(2:N))];
W = zeros(1, N); 
%Give rotation 

L = 1.2 * max(X);
t_max = 5;
clock_max = 1000;
dt = t_max / clock_max;
X_save = zeros(clock_max, N);
Y_save = zeros(clock_max, N);
Z_save = zeros(clock_max, N);

figure;
set(gcf, 'double', 'on')
colormat = [1 0 0; repmat( [0 0 1], N - 1, 1)]; %repmat repeats row 0 0 1, N - 1 times

subplot(2, 2, 1) %2x2 matrix of plots
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h1 = scatter(X, Y, 50 * ones(N, 1), colormat, 'fill');
xlabel('X')
ylabel('Y')
axis([-L L -L L])
axis equal
axis manual
hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h1_2 = plot([X;X], [Y;Y]);

subplot(2, 2, 2)
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h2 = scatter(Z, Y, 50 * ones(N, 1), colormat, 'fill');
xlabel('Z')
ylabel('Y')
axis([-L L -L L])
axis equal
axis manual
hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h2_2 = plot([Z;Z], [Y;Y]);

subplot(2, 2, 3)
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h3 = scatter(X, Z, 50 * ones(N, 1), colormat, 'fill');
xlabel('X')
ylabel('Z')
axis([-L L -L L])
axis equal
axis manual
hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h3_3 = plot([X;X], [Z;Z]);

subplot(2, 2, 4)
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h4 = scatter3(X, Y, Z, 50 * ones(N, 1), colormat, 'fill');
xlabel('X')
ylabel('Y')
zlabel('Z')
xlim([-L L]); ylim([-L L]); zlim([-L L]);
axis manual
hold on
h4_4 = plot([X;X], [Y;Y], [Z;Z]);


for clock = 1:clock_max
    for i = 1:N
        for j = 1:N
            if (i ~= j)
                R = sqrt( (X(j) - X(i))^2 + (Y(j) - Y(i))^2 + (Z(j) - Z(i))^2);
                U(i) = U(i) * dt * G * M(j) * (X(j) - X(i))/R^3;
                V(i) = V(i) * dt * G * M(j) * (Y(j) - Y(i))/R^3;
                W(i) = W(i) * dt * G * M(j) * (Z(j) - Z(i))/R^3;
            end
        end
    end
    
    for i = 1:N
            X(i) = X(i) + dt * U(i);
            Y(i) = Y(i) + dt * V(i);
            Z(i) = Z(i) + dt * W(i);
    end
    X_save(clock, :) = X;
    Y_save(clock, :) = Y;
    Z_save(clock, :) = Z;
    
    set(h1, 'xdata', X, 'ydata', Y)
    set(h1_2, {'XData'}, num2cell(X_save(1:clock_max, :)', 2), {'YData'}, num2cell(Y_save(1:clock_max, :)', 2))
    set(h2, 'xdata', Z, 'ydata', Y)
    set(h2_2, {'XData'}, num2cell(Z_save(1:clock_max, :)', 2), {'YData'}, num2cell(Y_save(1:clock_max, :)', 2))
    set(h3, 'xdata', X, 'ydata', Z)
    set(h3_3, {'XData'}, num2cell(X_save(1:clock_max, :)', 2), {'YData'}, num2cell(Z_save(1:clock_max, :)', 2))
    set(h4, 'xdata', X, 'ydata', Y, 'zdata', Z)
    set(h4_4, {'XData'}, num2cell(X_save(1:clock_max, :)', 2), {'YData'}, num2cell(Y_save(1:clock_max, :)', 2), {'ZData'}, num2cell(Z_save(1:clock_max, :)', 2))
    drawnow
end
    
    
    