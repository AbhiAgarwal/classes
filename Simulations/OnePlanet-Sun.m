% One-body Orbits
% Init

clear all;
close all;

%In Astronomical Units
G = 1;
Ms = 1; 
x = 1; %Earth at distance of 1 AU
y = 0;
u = 0;
v = sqrt(G * Ms / x) * 0.9;
L = 1.2 * x;
t_max = 5;
clock_max = 1000;
dt = t_max / clock_max;
X_save = zeros(clock_max, 1);
Y_save = zeros(clock_max, 1);

figure;
set(gcf, 'double', 'on')
h = scatter([0, x], [0, y], [100;100], [1 0 0; 0 0 1], 'fill');

axis([-L L -L L])
axis equal
axis manual

hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h2 = plot(x, y);

%Use X_save, Y_save to trace trajectory
for clock = 1:clock_max
    R = sqrt(x^2 + y^2);
    u = u - dt * G * Ms * x / R^3;
    v = v - dt * G * Ms * y / R^3;
    x = x + dt * u;
    y = y + dt * v;
    X_save(clock) = x;
    Y_save(clock) = y;
    set(h, 'xdata', [0, x], 'ydata', [0, y])
    set(h2, 'xdata', X_save(1:clock), 'ydata', Y_save(1:clock))
    drawnow
end