clear all;
close all;


% ---------- Variables declare and init'd ---------- %

% ------ Declared ------ %
numPoints = 4;

time_simulated = 500;
time_lapse = 10000;
time_step = time_simulated / time_lapse;

X = zeros(1, numPoints);
Y = zeros(1, numPoints);
Z = zeros(1, numPoints);

SX = zeros(1, numPoints);
SY = zeros(1, numPoints);
SZ = zeros(1, numPoints);


U = zeros(1, numPoints);
V = zeros(1, numPoints);
W = zeros(1, numPoints);

xForce = zeros(1, numPoints);
yForce = zeros(1, numPoints);
zForce = zeros(1, numPoints);

X_save = zeros(time_lapse, numPoints);
Y_save = zeros(time_lapse, numPoints);
Z_save = zeros(time_lapse, numPoints);

centre_mass = zeros(3, 1);
prev_spin = zeros(1, 3);
curr_spin = zeros(1, 3);
total_ang_momentum = zeros(1, 3);


CM = zeros(1, time_lapse);  %Total angular momentum
S = zeros(1, time_lapse);   %Spin angular momentum

%Distance from each point mass to Earth
tensionForce = 0;
distEarth = 0;
length = 0;
rateChangeLength = 0;

% ------ Initialized ------ %
initialRadius = 3.84748E5;
gravConst = 6.67E-11;
springConst = 4.0E11; %Most common rock on a satellite similar to the Moon is igneous
radiusSatellite = 1.73E4;
massPri = 5.98E24;
massSec = 7.35E22 * 1E10;
pointMass = massSec / 4;
beta = 2 * sqrt(massSec * springConst);
L = 1.2 * initialRadius;

% ---------- Tetrahedron constructed ---------- %
restLength = zeros(numPoints, numPoints);

X(1) = 0; Y(1) = 0; Z(1) = radiusSatellite;

phi = -19.471220333;
increment = degtorad(120);
phiRad = degtorad(phi);
theta = 0;

for i = 2:numPoints
    X(i) = radiusSatellite * cos(theta) * cos(phiRad);
    Y(i) = radiusSatellite * sin(theta) * cos(phiRad);
    Z(i) = radiusSatellite * sin(phiRad);
    theta = theta + increment;
end

for i = 1:numPoints
    X(i) = X(i) + initialRadius;
    U(i) = 0;
    V(i) = sqrt(gravConst * massPri / sqrt(X(i)^2 + Y(i)^2 + Z(i)^2));
    W(i) = 0;
end

% V(1) = sqrt(gravConst * massPri / initialRadius); W(1) = 0; U(1) = 0;
% V(2) = sqrt(gravConst * massPri / initialRadius); W(2) = 0; U(2) = 0;
% V(3) = sqrt(gravConst * massPri / initialRadius); W(3) = 0; U(3) = 0;
% V(4) = sqrt(gravConst * massPri / initialRadius); W(4) = 0; U(4) = 0;

 
%Translate vertices to centre of orbit at a distance given by the orbital
%radius.
%for i = 1:numPoints
 %  X(i) = X(i) + initialRadius;    
%end

centre_mass = [mean(X) mean(Y) mean(Z)];

for i = 1:numPoints
    Vel = [U(i) V(i) W(i)];
    Vel = Vel + cross([0 0 pi/3], ([X(i) Y(i) Z(i)] - centre_mass));
    U(i) = Vel(1);
    V(i) = Vel(2);
    W(i) = Vel(3);
end



%---------- Graphing ---------- %



figure;
set(gcf, 'double', 'on')
colormat = [1 0 0; repmat( [0 0 1], numPoints - 1, 1)]; %repmat repeats row 0 0 1, N - 1 times

subplot(3, 2, 1) %2x2 matrix of plots
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h1 = scatter(X, Y, 50 * ones(numPoints, 1), colormat, 'fill');
xlabel('X')
ylabel('Y')
axis([-L L -L L])
axis equal
axis manual
hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h1_1 = plot([X;X], [Y;Y]);

subplot(3, 2, 2)
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h2 = scatter(Z, Y, 50 * ones(numPoints, 1), colormat, 'fill');
xlabel('Z')
ylabel('Y')
axis([-L L -L L])
axis equal
axis manual
hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h2_2 = plot([Z;Z], [Y;Y]);

subplot(3, 2, 3)
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h3 = scatter(X, Z, 50 * ones(numPoints, 1), colormat, 'fill');
xlabel('X')
ylabel('Z')
axis([-L L -L L])
axis equal
axis manual
hold on
plot(xlim, [0 0], 'black')
plot([0 0], ylim, 'black')
h3_3 = plot([X;X], [Z;Z]);

%3D plot
subplot(3, 2, 4)
set(gca, 'NextPlot', 'replacechildren')
set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
h4 = scatter3(X, Y, Z, 50 * ones(numPoints, 1), colormat, 'fill');
xlabel('X')
ylabel('Y')
zlabel('Z')
xlim([-L L]); ylim([-L L]); zlim([-L L]);
axis manual
hold on
h4_4 = plot3([X;X], [Y;Y], [Z;Z]);

% subplot(3, 2, 5)
% set(gca, 'NextPlot', 'replacechildren')
% set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
% h5 = scatter3(SX, SY, SZ, 50 * ones(numPoints, 1), colormat, 'fill');
% xlabel('Spin-X')
% ylabel('Spin-Y')
% zlabel('Spin-Z')
% xlim([-L L]); ylim([-L L]); zlim([-L L]);
% axis manual
% hold on
% h5_5 = plot3([SX;SX], [SY;SY], [SZ;SZ]);

% subplot(3, 2, 5)
% set(gca, 'NextPlot', 'replacechildren')
% set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
% xlabel('T')
% ylabel('dSpin / dt')
% axis([-L L -L L])
% axis equal
% axis manual
% hold on
% plot(xlim, [0 0], 'black')
% plot([0 0], ylim, 'black')
% h5 = plot([T;T], [S;S]);
% 
% subplot(3, 2, 6)
% set(gca, 'NextPlot', 'replacechildren')
% set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
% xlabel('T')
% ylabel('Total angular momentum')
% axis([-L L -L L])
% axis equal
% axis manual
% hold on
% plot(xlim, [0 0], 'black')
% plot([0 0], ylim, 'black')
% h6 = plot([T;T], [CM;CM]);



Tetra=[1 2 3; 1 2 4; 2 3 4; 1 3 4];
tetra = trisurf(Tetra,X,Y,Z);

% ---------- Update loop ---------- %

for clock = 1:time_lapse
    %Compute Hooke's law and dashpot contributions to 
    %internal tension of the satellite
    
    
    for i = 1:numPoints
        xForce(i) = 0; yForce(i) = 0; zForce(i) = 0;    
        for j = 1:numPoints
            
            if (i ~= j)
                
                length = sqrt((X(j)- X(i))^2 + (Y(j)- Y(i))^2 + (Z(j)- Z(i))^2);
                
                rateChangeLength = (U(j) - U(i)).*(X(j) - X(i))./length + (V(j) - V(i)).*(Y(j) - Y(i))./length + (W(j) - W(i)).*(Z(j) - Z(i))./length;
                tensionForce = springConst * (length - restLength(i, j)) + beta * rateChangeLength;
                
                xForce(i) = xForce(i) + tensionForce * (X(j) - X(i))/length;
                yForce(i) = yForce(i) + tensionForce * (Y(j) - Y(i))/length;
                zForce(i) = zForce(i) + tensionForce * (Z(j) - Z(i))/length;

            end
        end
    end
    
    
    %Compute total angular momentum and spin momentum
    
    centre_mass = [mean(X) mean(Y) mean(Z)];
    
    for i = 1:numPoints
        curr_spin = curr_spin + cross(([X(i) Y(i) Z(i)] - centre_mass), pointMass * [U(i) V(i) W(i)]);
    end
    
    for i = 1:numPoints
        total_ang_momentum = total_ang_momentum + cross([X(i) Y(i) Z(i)], (pointMass * [U(i) V(i) W(i)]));
    end
    
    %S(clock) = (norm(curr_spin - prev_spin)) / time_step;
    S(clock) = norm(curr_spin);
    SX(clock) = norm(curr_spin(1));
    SY(clock) = norm(curr_spin(2));
    SZ(clock) = norm(curr_spin(3));
    CM(clock) = norm(total_ang_momentum);
    prev_spin = curr_spin;
    
    total_ang_momentum = zeros(1, 3);
    curr_spin=zeros(1,3);
%   
    
    %Update velocities of each vertex
    for i = 1:numPoints
        distEarth = sqrt(X(i)^2 + Y(i)^2 + Z(i)^2);
        U(i) = U(i) - time_step * gravConst * massPri * X(i)/distEarth^3 + time_step * xForce(i) / pointMass;
        V(i) = V(i) - time_step * gravConst * massPri * Y(i)/distEarth^3 + time_step * yForce(i) / pointMass;
        W(i) = W(i) - time_step * gravConst * massPri * Z(i)/distEarth^3 + time_step * zForce(i) / pointMass;
    end
    
%      for i = 1:numPoints
%          Rotated= R*[X(i)-centre_mass(1) Y(i)-centre_mass(2) Z(i)]';
%          X(i) = centre_mass(1) + Rotated(1);
%          Y(i) = centre_mass(2) + Rotated(2);
%          Z(i) = Rotated(3);
%      end
    

    
    %Update positions of each vertex
    for i = 1:numPoints
        X(i) = X(i) + time_step * U(i);
        Y(i) = Y(i) + time_step * V(i);
        Z(i) = Z(i) + time_step * W(i);
    end
    

    X_save(clock, :) = X;
    Y_save(clock, :) = Y;
    Z_save(clock, :) = Z;
    delete(tetra);
    tetra = trisurf(Tetra,X,Y,Z);
    
    set(h1, 'xdata', X, 'ydata', Y)
    set(h1_1, {'XData'}, num2cell(X_save(1:time_lapse, :)', 2), {'YData'}, num2cell(Y_save(1:time_lapse, :)', 2))
    set(h2, 'xdata', Z, 'ydata', Y)
    set(h2_2, {'XData'}, num2cell(Z_save(1:time_lapse, :)', 2), {'YData'}, num2cell(Y_save(1:time_lapse, :)', 2))
    set(h3, 'xdata', X, 'ydata', Z)
    set(h3_3, {'XData'}, num2cell(X_save(1:time_lapse, :)', 2), {'YData'}, num2cell(Z_save(1:time_lapse, :)', 2))
    set(h4, 'xdata', X, 'ydata', Y, 'zdata', Z)
    set(h4_4, {'XData'}, num2cell(X_save(1:time_lapse, :)', 2), {'YData'}, num2cell(Y_save(1:time_lapse, :)', 2), {'ZData'}, num2cell(Z_save(1:time_lapse, :)', 2))
    %set(h5, 'xdata', SX, 'ydata', SY, 'zdata', SZ)
    %set(h5_5, {'XData'}, num2cell(X_save(1:time_lapse, :)', 2), {'YData'}, num2cell(Y_save(1:time_lapse, :)', 2), {'ZData'}, num2cell(Z_save(1:time_lapse, :)', 2))
    %set(h5, 'xdata', T, 'ydata', S)
    %set(h6, 'xdata', T, 'ydata', CM)
    drawnow
end


% figure;
% set(gcf, 'double', 'on')
% 
% subplot(2, 1, 1)
% set(gca, 'NextPlot', 'replacechildren')
% %set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
% xlabel('T')
% ylabel('dSpin / dt')
% axis([-L L -L L])
% axis equal
% axis manual
% hold on
% plot(xlim, [0 0], 'black')
% plot([0 0], ylim, 'black')
% h5 = plot(S);
% 
% subplot(2, 1, 2)
% set(gca, 'NextPlot', 'replacechildren')
% %set(gca, 'ColorOrder', colormat); %Each trajectory has a unique color
% xlabel('T')
% ylabel('Total angular momentum')
% axis([-L L -L L])
% axis equal
% axis manual
% hold on
% plot(xlim, [0 0], 'black')
% plot([0 0], ylim, 'black')
% h6 = plot(CM);