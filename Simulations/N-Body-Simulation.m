clear all;
G = 1;
N = 5;
M = [9 8 7 6 5];
X = [1 2 3 4 5];
Y = zeros(1, N);
Z = zeros(1, N);
U = zeros(1, N);
V = [0 sqrt(G * M(1)./X(2:N))]
W = zeros(1, N);
tmax = 5; % physical time
clockmax = 1000; % number of steps
dt = tmax/clockmax; % divide that stuff
L = 1.2*N;

set(gcf, 'double', 'on');
% plot 1
subplot(2, 2, 1), h1=plot(X, Y, 'bo');
axis([-L, L, -L, L]);
axis equal;
axis manual;
% plot 2
subplot(2, 2, 2), h2=plot(Z, Y, 'bo');
axis([-L, L, -L, L]);
axis equal;
axis manual;
% plot 3
subplot(2, 2, 3), h3=plot(X, Z, 'bo');
axis([-L, L, -L, L]);
axis equal;
axis manual;
% plot 4
subplot(2, 2, 4), h4=plot3(X, Y, Z, 'bo');
axis([-L, L, -L, L]);
% axis equal; %??? causes trouble
axis manual;

for clock = 1:clockmax
    for i = 1:N % updating rate of change of velocity
       for j = 1:N
          if (i ~= j)
              R = sqrt((X(j)-X(i))^2 + (Y(j)-Y(i)^2 + Z(j)-Z(j))^2); % calculating the R according to i&j values
              U(i) = U(i) + dt*G*M(j)*(X(j)-X(i))/R^2; % updating U array
              V(i) = V(i) + dt*G*M(j)*(Y(j)-Y(i))/R^2; % updating V array
              W(i) = W(i) + dt*G*M(j)*(Z(j)-Z(i))/R^2; % updating W array
          end
       end
    end
    for i = 1:N % positional update
        X(i) = X(i) + dt*U(i); % updating the position of X
        Y(i) = Y(i) + dt*V(i); % updating the position of Y
        Z(i) = Z(i) + dt*W(i); % updating the position of Z
    end
    set(h1, 'xdata', X, 'ydata', Y);
    set(h2, 'xdata', Z, 'ydata', Y);
    set(h3, 'xdata', X, 'ydata', Z);
    set(h4, 'xdata', X, 'ydata', Y, 'zdata', Z);
    drawnow
end